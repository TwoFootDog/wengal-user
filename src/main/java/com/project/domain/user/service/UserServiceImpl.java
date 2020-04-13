package com.project.domain.user.service;

import com.project.domain.user.model.dto.CommonResult;
import com.project.domain.user.model.dto.SignUpRequest;
import com.project.domain.user.model.dto.SingleResult;
import com.project.domain.user.model.entity.User;
import com.project.domain.user.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    /* 회원 등록 함수 */
    public SingleResult<User> signUp(SignUpRequest request) throws Exception {
        SingleResult<User> result = new SingleResult<>();
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                "signup",
                new Date(),
                "signup",
                new Date());
        try {
            result = getSingleResult(userRepository.save(user));
        } catch (Exception e) {
            logger.info(">>>>>>>>>>>>>>>>>>>>>Exception>>>>>>>>>>>>>>>>>>>>>>");
            e.printStackTrace();
        }
        return result;
    }

    /* 결과파일 전송 */
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<T>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    /* 성공 시 메시지 및 코드 셋팅하는 함수 */
    private void setSuccessResult(CommonResult result) {
        result.setCode(0);
        result.setMsg("SUCCESS");
        result.setSuccess(true);
    }
}
