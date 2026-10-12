package com.tenco.spring_bblog.user;

import com.tenco.spring_bblog._core.error.Exception400;
import com.tenco.spring_bblog._core.error.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션
@Service
public class UserService {

    private final UserJpaRepository userJpaRepository;

    @Transactional // 쓰기 작업이라서 트랜잭션 활성화
    public User join(UserRequest.JoinDto joinDto) {
        log.info("회원가입 서비스 처리 시작 - 사용자명: {}", joinDto.getUsername());
        // 중복 체크
        userJpaRepository.findByUsername(joinDto.getUsername())
                .ifPresent(user -> {
                    log.warn("회원가입 실패 - 중복된 사용자명: {}", joinDto.getUsername());
                    throw new Exception400("이미 존재하는 사용자명입니다.");
                });


        // 이메일 중복 검사
        userJpaRepository.findByEmail(joinDto.getEmail())
                .ifPresent(user -> {
                    log.warn("회원가입 실패 - 중복된 이메일: {}", joinDto.getEmail());
                    throw new Exception400("이미 사용 중인 이메일입니다.");
                });

        // 저장
        User savedUser = userJpaRepository.save(joinDto.toEntity());
        log.info("회원가입 서비스 처리 완료 - 사용자명: {}, ID: {}", joinDto.getUsername(), savedUser.getId());
        return savedUser;
    }

    public User login(UserRequest.LoginDto loginDto) {
        log.info("로그인 서비스 처리 시작 - 사용자명: {}", loginDto.getUsername());

        User user = userJpaRepository.findByUsernameAndPassword(
                loginDto.getUsername(), loginDto.getPassword())
                .orElseThrow(() ->{
                    log.warn("로그인 실패 - 사용자명: {}, 원인: 인증정보 불일치", loginDto.getUsername());
                    throw new Exception400("사용자명 또는 비밀번호가 올바르지 않습니다.");
                });
        log.info("로그인 성공 - 사용자명: {}", loginDto.getUsername());
        return user;
    }

    public User findById(Long id) {
        log.info("사용자 정보 조회 - ID: {}", id);
        return userJpaRepository.findById(id)
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public User updateById(Long id, UserRequest.UpdateDto updateDto) {
        User user = findById(id);
        user.update(updateDto.getPassword());
        return user;
    }
}
