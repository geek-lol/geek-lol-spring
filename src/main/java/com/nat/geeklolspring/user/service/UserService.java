package com.nat.geeklolspring.user.service;

import com.nat.geeklolspring.auth.TokenProvider;
import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.boardBulletinReply.repository.BoardReplyRepository;
import com.nat.geeklolspring.aws.S3Service;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.board.vote.repository.BoardVoteCheckRepository;
import com.nat.geeklolspring.entity.Role;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.game.repository.CsGameRankRepository;
import com.nat.geeklolspring.game.repository.ResGameRankRepository;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.shorts.shortsreply.repository.ShortsReplyRepository;
import com.nat.geeklolspring.shorts.vote.repository.VoteCheckRepository;
import com.nat.geeklolspring.troll.apply.repository.ApplyReplyRepository;
import com.nat.geeklolspring.troll.apply.repository.ApplyVoteCheckRepository;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.troll.ruling.repository.RulingReplyRepository;
import com.nat.geeklolspring.troll.ruling.repository.RulingVoteRepository;
import com.nat.geeklolspring.user.dto.request.*;
import com.nat.geeklolspring.user.dto.response.*;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Value("${upload.path}")
    private String rootPath;

    private final List<SocialLoginService> loginServices;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final BoardRulingRepository boardRulingRepository;
    private final BoardBulletinRepository boardBulletinRepository;
    private final RulingApplyRepository rulingApplyRepository;
    private final ShortsRepository shortsRepository;

    private final CsGameRankRepository csGameRankRepository;
    private final ResGameRankRepository resGameRankRepository;

    private final ApplyVoteCheckRepository applyVoteCheckRepository;
    private final RulingVoteRepository rulingVoteRepository;
    private final VoteCheckRepository shortsVoteRepository;
    private final BoardVoteCheckRepository boardVoteCheckRepository;

    private final RulingReplyRepository rulingReplyRepository;
    private final ApplyReplyRepository applyReplyRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final ShortsReplyRepository shortsReplyRepository;
    private final S3Service s3Service;


    public UserResponseDTO findByUserInfo(TokenUserInfo userInfo) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        return new UserResponseDTO(user);

    }

    public UserSignUpResponseDTO create(UserSignUpRequestDTO dto) throws IOException {

        if (dto == null) {
            throw new RuntimeException("회원가입 입력정보가 없습니다!");
        }
        String id = dto.getId();

        if (userRepository.existsById(id)) {
            log.warn("아이디가 중복되었습니다!! - {}", id);
            throw new RuntimeException("중복된 아이디입니다!!");
        }

        User saved = userRepository.save(dto.toEntity(passwordEncoder));

        //File defaultImageFile = new File("/public/defaultUser.jpg");
        Path filePath = Paths.get("src/main/resources/public", "defaultUser.jpg").toAbsolutePath();
        MultipartFile multipartDefaultFile = getMultipartDefaultFile(filePath.toFile());

        String s = uploadProfileImage(multipartDefaultFile);
        saved.setProfileImage(s);

        log.info("회원가입 성공!! saved user - {}", saved);

        return new UserSignUpResponseDTO(saved);

    }

    private MultipartFile getMultipartDefaultFile(File file) throws IOException {
        //File file = new File(new File("").getAbsoluteFile() + "public/defaultUser.jpg");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        FileItem fileItem = new DiskFileItem("originFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

        try {
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
        } catch (IOException e) {
            log.error("File to Multipart Error : {}", e.getMessage());
            throw e;
        }

        MultipartFile mFile = new CommonsMultipartFile(fileItem);
        return mFile;
    }

    public LoginResponseDTO doSocialLogin(SocialLoginRequestDTO request) throws ChangeSetPersister.NotFoundException, IOException {
        SocialLoginService loginService = this.getLoginService("google");

        SocialAutoResponseDTO socialAuthResponse = loginService.getAccessToken(request.getCode());

        SocialUserResponseDTO socialUserResponse = loginService.getUserInfo(socialAuthResponse.getAccess_token());
        log.info("socialUserResponse {} ", socialUserResponse.toString());

        if (userRepository.findById(socialUserResponse.getId()).isEmpty()) {
            this.create(
                    UserSignUpRequestDTO.builder()
                            .id(socialUserResponse.getEmail())
                            .userName(socialUserResponse.getName())
                            .build()
            );
        }

        User user = userRepository.findById(socialUserResponse.getId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        return LoginResponseDTO.builder()
                .id(user.getId())
                .build();
    }

    private SocialLoginService getLoginService(String userType) {
        for (SocialLoginService loginService : loginServices) {
            if (userType.equals("google")) {
                log.info("login service name: {}", "google");
                return loginService;
            }
        }
        return new GoogleLoginServiceImpl();
    }


    @Transactional
    public void delete(UserDeleteRequestDTO dto) {

        if (!userRepository.existsById(dto.getId())) {
            log.warn("삭제할 회원이 없습니다!! - {}", dto.getId());
            throw new RuntimeException("중복된 아이디입니다!!");
        }
        if (dto.getIds() != null) {
            dto.getIds().forEach(id -> {
                User user = userRepository.findById(id).orElseThrow();
//                deleteChildren(user);
                userRepository.delete(user);
            });
        } else {
            User user = userRepository.findById(dto.getId()).orElseThrow();
//            deleteChildren(user);
            userRepository.delete(user);
        }
    }

    public void delete(List<String> ids) {
        if (ids == null) {
            log.warn("삭제할 회원이 없습니다!!");
        }
        try {
            assert ids != null;
            ids.forEach(id -> {
                User user = userRepository.findById(id).orElseThrow();
//                deleteChildren(user);
                userRepository.delete(user);
            });
        } catch (NullPointerException e) {
            throw new RuntimeException("삭제 안도ㅐ유");
        }

    }

    void deleteChildren(User user) {

        boardReplyRepository.deleteAllByWriterUser(user);
        rulingReplyRepository.deleteAllByRulingWriterId(user);
        applyReplyRepository.deleteAllByUserId(user);
        shortsReplyRepository.deleteAllByWriterId(user);

        rulingVoteRepository.deleteAllByRulingVoter(user);
        boardVoteCheckRepository.deleteAllByUser(user);
        applyVoteCheckRepository.deleteAllByReceiver(user);
        shortsReplyRepository.deleteAllByWriterId(user);

        boardBulletinRepository.deleteAllByUser(user);
        rulingApplyRepository.deleteAllByUserId(user);
        shortsRepository.deleteAllByUploaderId(user);
        boardRulingRepository.deleteAllByRulingPosterId(user);

        csGameRankRepository.deleteAllByUser(user);
        resGameRankRepository.deleteAllByUser(user);
    }

    public boolean isDupilcateId(String id) {
        return userRepository.existsById(id);
    }

    public boolean isDupilcatePw(String pw, TokenUserInfo userInfo) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        if (!passwordEncoder.matches(pw, user.getPassword())) {
            return false;
        }
        return true;
    }


    public LoginResponseDTO authenticate(final LoginRequestDTO dto) {

        log.info("dto : {}", dto);

        User user = userRepository.findById(dto.getId())
                .orElseThrow(
                        () -> new RuntimeException("가입된 회원이 아닙니다.")
                );

        String inputPassword = dto.getPassword();
        String encodedPassword = user.getPassword();

        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw new RuntimeException("비밀번호가 틀렸습니다");
        }
        String token = tokenProvider.createToken(user);

        user.setAutoLogin(dto.getAutoLogin());

        log.info("user : {}", user);

        userRepository.save(user);

        return new LoginResponseDTO(user, token);
    }


    @Transactional
    public LoginResponseDTO modify(TokenUserInfo userInfo, UserModifyRequestDTO dto, String profilePath) {
        // dto : password , profileIamge, userName = 수정할 수 있음
        // 하나씩 수정함
        // 수정할 것 제외는 원래 값을 넣어줘야함
        log.info("modifyDTO : {}, profilepath :{}", dto,profilePath);
        if (dto == null && profilePath == null) {
            throw new RuntimeException("수정된 회원정보가 없습니다!");
        }
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(()-> new RuntimeException("유저 정보가 없습니단."));
        UserModifyRequestDTO modifyRequestDTO = new UserModifyRequestDTO(user);

        //비밀번호를 수정해야하는 지 체크하는 변수
        boolean flag = false;

        assert dto != null;
        if (dto.getPassword() != null) {
            modifyRequestDTO.setPassword(dto.getPassword());
            flag = true;
        }
        if (profilePath != null) {
            modifyRequestDTO.setProfileIamge(profilePath);
        }
        if (dto.getUserName() != null) {
            modifyRequestDTO.setUserName(dto.getUserName());
        }
        log.info("수정된 user dto : {} ",modifyRequestDTO);
        User saved = null;

        if (flag) { //비밀번호 수정할때
            saved = userRepository.save(modifyRequestDTO.toEntity(passwordEncoder));
        } else {
            saved = userRepository.save(modifyRequestDTO.toEntity());
        }

        String token = tokenProvider.createToken(saved);

        log.info("회원정보 수정 성공!! saved user - {}", saved);

        return new LoginResponseDTO(saved, token);

    }

    public String uploadProfileImage(MultipartFile originalFile) throws IOException {

        // 루트 디렉토리가 존재하는지 확인 후 존재하지 않으면 생성한다
        //File rootDir = new File(rootPath);
        //if (!rootDir.exists()) rootDir.mkdirs();

        // 파일명을 유니크하게 변경
        String uniqueFileName = UUID.randomUUID() + "_" + originalFile.getOriginalFilename();

        // 파일을 서버에 저장
        //File uploadFile = new File(rootPath + "/" + uniqueFileName);
        //originalFile.transferTo(uploadFile);

        return s3Service.uploadUoS3Bucket(originalFile.getBytes(), uniqueFileName);
    }

    public String getProfilePath(String id) {

        //DB에서 파일명 조회
        User user = userRepository.findById(id).orElseThrow();

        return user.getProfileImage();

    }

    public void changeAuth(String userId, String newAuth) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        log.info("newAuth:{}", newAuth);
        try {
            switch (newAuth) {
                case "ADMIN":
                    userRepository.updateAuthority(userId, Role.ADMIN);
                    break;
                case "COMMON":
                    userRepository.updateAuthority(userId, Role.COMMON);
                    break;
                default:
                    throw new RuntimeException("없는 권한입니다.");
            }
        } catch (Exception e) {
            log.warn("권한 변경 에러! :{}", e.getMessage());
            throw new RuntimeException("회원 권한 변경에 실패하였습니다.");
        }
    }

}
