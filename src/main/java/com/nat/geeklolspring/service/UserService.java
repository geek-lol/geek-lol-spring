package com.nat.geeklolspring.service;

import com.nat.geeklolspring.dto.request.UserSignUpRequestDTO;
import com.nat.geeklolspring.dto.response.UserSignUpResponseDTO;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public boolean isDupilcateId(String id){
        return userRepository.existsById(id);
    }


}
