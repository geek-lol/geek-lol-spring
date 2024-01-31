package com.nat.geeklolspring.user.service;

import com.nat.geeklolspring.auth.TokenProvider;
import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.user.dto.request.LoginRequestDTO;
import com.nat.geeklolspring.user.dto.request.UserModifyRequestDTO;
import com.nat.geeklolspring.user.dto.request.UserSignUpRequestDTO;
import com.nat.geeklolspring.user.dto.response.LoginResponseDTO;
import com.nat.geeklolspring.user.dto.response.UserSignUpResponseDTO;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Value("${upload.path}")
    private String rootPath;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserSignUpResponseDTO create(UserSignUpRequestDTO dto) {

        if (dto == null) {
            throw new RuntimeException("회원가입 입력정보가 없습니다!");
        }
        String id = dto.getId();

        if (userRepository.existsById(id)) {
            log.warn("아이디가 중복되었습니다!! - {}", id);
            throw new RuntimeException("중복된 아이디입니다!!");
        }

        User saved = userRepository.save(dto.toEntity(passwordEncoder));

        log.info("회원가입 성공!! saved user - {}", saved);

        return new UserSignUpResponseDTO(saved);

    }

    public void delete(User user) {

        if (!userRepository.existsById(user.getId())) {
            log.warn("삭제할 회원이 없습니다!! - {}", user.getId());
            throw new RuntimeException("중복된 아이디입니다!!");
        }

        userRepository.delete(user);

    }

    public boolean isDupilcateId(String id){
        return userRepository.existsById(id);
    }

    public LoginResponseDTO authenticate(final LoginRequestDTO dto){

        User user = userRepository.findById(dto.getId())
                .orElseThrow(
                        () -> new RuntimeException("가입된 회원이 아닙니다.")
                );

        String inputPassword = dto.getPassword();
        String encodedPassword = user.getPassword();

        if (!passwordEncoder.matches(inputPassword,encodedPassword)){
            throw new RuntimeException("비밀번호가 틀렸습니다");
        }

        String token = tokenProvider.createToken(user);

        return new LoginResponseDTO(user,token);
    }



    public LoginResponseDTO modify(TokenUserInfo userInfo, UserModifyRequestDTO dto, String profilePath) {

        if (dto == null && profilePath == null) {
            throw new RuntimeException("수정된 회원정보가 없습니다!");
        }

        if (dto.getPassword() == null){
            dto.setPassword(userInfo.getPassword());
        }
        if (dto.getProfileIamge() == null){
            dto.setProfileIamge(userInfo.getProfileImage());
        }
        if (dto.getUserName() == null){
            dto.setUserName(userInfo.getUserName());
        }

        String userId = userInfo.getUserId();

        Optional<User> byId = userRepository.findById(userId);

        log.info("{}",byId);

//        delete(byId);

        User saved = userRepository.save(dto.toEntity(userId,passwordEncoder,profilePath,userInfo.getRole()));

        String token = tokenProvider.createToken(saved);

        log.info("회원정보 수정 성공!! saved user - {}", saved);

        return new LoginResponseDTO(saved,token);

    }



    public String uploadProfileImage(MultipartFile originalFile) throws IOException {

        // 루트 디렉토리가 존재하는지 확인 후 존재하지 않으면 생성한다
        File rootDir = new File(rootPath);
        if (!rootDir.exists()) rootDir.mkdirs();

        // 파일명을 유니크하게 변경
        String uniqueFileName = UUID.randomUUID() + "_" + originalFile.getOriginalFilename();

        // 파일을 서버에 저장
        File uploadFile = new File(rootPath + "/" + uniqueFileName);
        originalFile.transferTo(uploadFile);

        return uniqueFileName;
    }

    public String getProfilePath(String id){

        //DB에서 파일명 조회
        User user = userRepository.findById(id).orElseThrow();
        String fileName = user.getProfileImage();

        return rootPath+"/"+fileName;

    }


}
