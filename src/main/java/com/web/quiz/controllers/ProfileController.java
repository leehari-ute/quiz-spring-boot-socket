package com.web.quiz.controllers;

import com.web.quiz.constant.Role;
import com.web.quiz.models.Quiz;
import com.web.quiz.models.User;
import com.web.quiz.repositories.UserRepository;
import com.web.quiz.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final AuthService authService;
    private final SubjectService subjectService;
    private final QuizService quizService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GradeService gradeService;

    @Autowired
    public ProfileController(UserService userService,CloudinaryService cloudinaryService, GradeService gradeService, AuthService authService, SubjectService subjectService, QuizService quizService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authService=authService;
        this.subjectService = subjectService;
        this.quizService = quizService;
        this.passwordEncoder = passwordEncoder;
        this.gradeService = gradeService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping({"/profile"})
    public String getViewProfile(Model model, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> userOptional = this.userService.getUserByAuthentication(authentication);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);
                List<Quiz> quizList = this.quizService.findAllByIdOwner(user);
                model.addAttribute("quizzes", quizList);
                model.addAttribute("grades", this.gradeService.findByOrderByOrder());
                if (user.getRole().equalsIgnoreCase(Role.TEACHER)) {
                    return "profile/profile-teacher";
                }
                return "profile/profile-student";
            }
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/profile/update")
    public String update(@ModelAttribute User user, @RequestParam("fileAva") MultipartFile[] files, Authentication authentication){
        if (this.authService.isAuth()) {
            Optional<User> userOptional = this.userService.getUserByAuthentication(authentication);
            if (userOptional.isPresent()) {
                User userRecent = userOptional.get();
                userRecent.setName(user.getName());
                userRecent.setGrade(user.getGrade());
                userRecent.setBio(user.getBio());
                if (!files[0].isEmpty()) {
                    String imgUrl = this.cloudinaryService.uploadFile(files[0]);
                    userRecent.setAvt(imgUrl);
                }
                this.userService.saveUser(userRecent);
            }
            return "redirect:/profile";
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/setting")
    public String getViewSetting(Model model, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> userOptional = this.userService.getUserByAuthentication(authentication);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("user", user);
                if (user.getRole().equalsIgnoreCase(Role.TEACHER)) {
                    return "profile/setting-teacher";
                }
                return "profile/setting-student";
            }
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/setting/update-password")
    public String updatePassword(@RequestParam("newpassword") String newpassword,@RequestParam("passwordagain") String passwordagain
            ,Authentication authentication){
        if (this.authService.isAuth()) {
            Optional<User> userOptional = this.userService.getUserByAuthentication(authentication);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if(newpassword.equals(passwordagain)) {
                    user.setPassword(this.passwordEncoder.encode(newpassword));
                }
                this.userService.saveUser(user);
            }
        }
        return "redirect:/setting";
    }
}
