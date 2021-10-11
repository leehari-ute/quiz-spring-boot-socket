package com.web.quiz.controllers;

import com.web.quiz.models.*;
import com.web.quiz.services.*;
import com.web.quiz.utils.HelperUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class QuizRestController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final QuizService quizService;
    private final TimeQuestionService timeQuestionService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public QuizRestController(QuestionService questionService, CloudinaryService cloudinaryService, QuizService quizService, AnswerService answerService, TimeQuestionService timeQuestionService, UserService userService) {
        this.questionService = questionService;
        this.quizService = quizService;
        this.answerService = answerService;
        this.timeQuestionService = timeQuestionService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/add-question")
    public Response addQuestion(@RequestBody RequestBodySaveQuestion body, @RequestParam(value = "id") Integer idQuiz, Authentication authentication) {
//        System.out.println(body);
//        System.out.println(idQuiz);
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), idQuiz)) {
                Optional<Quiz> quizOptional = this.quizService.findById(idQuiz);
                if (quizOptional.isPresent()) {
                    Quiz quiz = quizOptional.get();
                    Question question = body.getQuestion();
                    question.setQuiz(quiz);

                    Integer idQuestion = Integer.parseInt(HelperUtils.generateID(9));
                    while (this.questionService.findByPrimaryKey(new Question.PrimaryKey(idQuestion, idQuiz)).isPresent()) {
                        idQuestion = Integer.parseInt(HelperUtils.generateID(9));
                    }
                    question.setPrimaryKey(new Question.PrimaryKey(idQuestion, idQuiz));

                    Question question1 = this.questionService.insertQuestion(question);
                    System.out.println(question1);
                    for (Answer value : body.getAnswers()) {
                        value.setQuestion(question1);
                        value.getPrimaryKey().setQuestionPrimaryKey(question1.getPrimaryKey());
                        System.out.println(value);
                        this.answerService.insertAnswer(value);
                    }
                    long questionAmount = this.questionService.countQuestionInQuiz(quiz);
                    quiz.setNumberQuestions((int) questionAmount);
                    this.quizService.insertQuiz(quiz);
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("questionAmount", questionAmount);
                    jsonResponse.put("idQues", question1.getPrimaryKey().getId());
                    return new Response("Successfully", 200, true, jsonResponse.toString());
                }
            }
            return new Response("Forbidden", 403, false, "");
        }
        return new Response("Error", 500, false, "");
    }

    @PostMapping("/update-question")
    public Response updateQuestion(@RequestBody RequestBodySaveQuestion body,
                                   @RequestParam(value = "idQuiz") Integer idQuiz,
                                   @RequestParam(value = "idQues") Integer idQues,
                                   Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), idQuiz)) {
                Optional<Question> question = this.questionService.findByPrimaryKey(new Question.PrimaryKey(idQues, idQuiz));
                System.out.println(body);
                if (question.isPresent()) {
                    Question questionUpdate = body.getQuestion();
                    if (question.get().getPrimaryKey().equals(questionUpdate.getPrimaryKey())) {
                        Question insertedQuestion = this.questionService.insertQuestion(questionUpdate);
                        for (Answer value : body.getAnswers()) {
                            value.setQuestion(insertedQuestion);
//                    value.getPrimaryKey().setQuestionPrimaryKey(insertedQuestion.getPrimaryKey());
                            System.out.println(value);
                            this.answerService.insertAnswer(value);
                        }
                        return new Response("Successfully", 200, true, idQues);
                    }
                }
            }
            return new Response("Forbidden", 403, false, "");
        }
        return new Response("Error", 500, false, "");
    }

    @PostMapping("/update-duration-question/{idQuiz}/{idQues}")
    public Response updateQuestion(@RequestBody Integer duration,
                                   @PathVariable Integer idQuiz,
                                   @PathVariable Integer idQues,
                                   Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), idQuiz)) {
                Optional<Question> question = this.questionService.findByPrimaryKey(new Question.PrimaryKey(idQues, idQuiz));
                if (question.isPresent()) {
                    Question update = question.get();
                    update.setDuration(duration);
                    this.questionService.insertQuestion(update);
                    return new Response("Successfully", 200, true, "");
                }
            }
            return new Response("Forbidden", 403, false, "");
        }
        return new Response("Error", 500, false, "");
    }

    @GetMapping("/get-saved-view/{idQuiz}/{idQues}/{order}")
    public ModelAndView getSavedView(@PathVariable Integer idQues,
                                     @PathVariable Integer idQuiz,
                                     @PathVariable Integer order,
                                     Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), idQuiz)) {
                Question.PrimaryKey primaryKey = new Question.PrimaryKey(idQues, idQuiz);
                Optional<Question> question = this.questionService.findByPrimaryKey(primaryKey);
                if (question.isPresent()) {
                    List<TimeQuestion> timeQuestions = this.timeQuestionService.getAll();
                    ModelAndView modelAndView = new ModelAndView("quiz/question-section");
                    modelAndView.addObject("question", question.get());
                    modelAndView.addObject("times", timeQuestions);
                    modelAndView.addObject("order", order);
                    return modelAndView;
                }
            }
        }
        return new ModelAndView();
    }

    @GetMapping("/get-question/{idQuiz}/{idQues}")
    public @ResponseBody Response getQuestion(@PathVariable Integer idQues,
                                              @PathVariable Integer idQuiz,
                                              Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), idQuiz)) {
                Optional<Question> question = this.questionService.findByPrimaryKey(new Question.PrimaryKey(idQues, idQuiz));
                if (question.isPresent()) {
                    RequestBodySaveQuestion response = new RequestBodySaveQuestion(question.get(), question.get().getAnswers());
                    return new Response("Successfully", 200, true, response);
                }
            }
            return new Response("Forbidden", 403, false, "");
        }
        return new Response("Error", 500, false, "");
    }

    @PostMapping("/delete-question/{idQuiz}/{idQues}")
    public Response deleteQuestion(@PathVariable Integer idQues,
                                   @PathVariable Integer idQuiz,
                                   Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), idQuiz)) {
                Question.PrimaryKey primaryKey = new Question.PrimaryKey(idQues, idQuiz);
                Optional<Question> question = this.questionService.findByPrimaryKey(primaryKey);
                if (question.isPresent()) {
                    this.questionService.deleteQuestionByPrimaryKey(primaryKey);
                    question = this.questionService.findByPrimaryKey(primaryKey);
                    if (!question.isPresent()) {
                        Optional<Quiz> quiz = this.quizService.findById(idQuiz);
                        if (quiz.isPresent()) {
                            Quiz quizUpdate = quiz.get();
//                            System.out.println(quizUpdate);
                            long questionAmount = this.questionService.countQuestionInQuiz(quizUpdate);
                            quizUpdate.setNumberQuestions((int) questionAmount);
//                            this.quizService.insertQuiz(quizUpdate);
                            return new Response("Successfully", 200, true, questionAmount);
                        }
                    }
                    return new Response("Error", 500, false, "");
                }
            }
            return new Response("Forbidden", 403, false, "");
        }
        return new Response("Error", 500, false, "");
    }

    @PostMapping("/save-quiz")
    public Response saveQuiz(@ModelAttribute RequestBodySaveQuiz body, @RequestParam("fileQuiz") MultipartFile[] files, Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), body.getId())) {
                System.out.println(files.length);
                Optional<Quiz> quiz = this.quizService.findByIdAndIdOwner(body.getId(), user.get());
                if (quiz.isPresent()) {
                    Quiz quizSave = quiz.get();
                    quizSave.setStatus(body.getStatus());
                    quizSave.setSubject(new Subject(body.getSubject()));
                    quizSave.setFromGrade(body.getFromGrade());
                    quizSave.setToGrade(body.getToGrade());
                    quizSave.setName(body.getName());
                    if (!files[0].isEmpty()) {
                        String imgUrl = this.cloudinaryService.uploadFile(files[0]);
                        quizSave.setTitleImage(imgUrl);
                    }
                    Quiz saved = this.quizService.insertQuiz(quizSave);
                    return new Response("Successfully", 200, true, saved);
                }
                return new Response("Not found quiz", 404, false, "");
            }
            return new Response("Forbidden", 403, false, "");
        }
        return new Response("Error", 500, false, "");
    }

    @PostMapping("/upload-image-question")
    public Response uploadImageQuestion(@RequestParam("image-question") MultipartFile[] files, @RequestParam(value = "idQuiz") Integer idQuiz, @RequestParam(value = "idQues") Integer idQues, Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.isHasOwnQuiz(user.get(), idQuiz)) {
                Optional<Quiz> quiz = this.quizService.findByIdAndIdOwner(idQuiz, user.get());
                if (quiz.isPresent()) {
                    Optional<Question> question = this.questionService.findByPrimaryKey(new Question.PrimaryKey(idQues, idQuiz));
                    if (question.isPresent()) {
                        if (!files[0].isEmpty()) {
                            String imgUrl = this.cloudinaryService.uploadFile(files[0]);
                            Question save = question.get();
                            save.setFile(imgUrl);
                            Question saved = this.questionService.insertQuestion(save);
                            return new Response("Successfully", 200, true, saved);
                        }
                    }
                }
            }
        }
        return new Response("Error", 500, false, "");
    }

    @PostMapping("/publish-quiz")
    public Response publishQuiz(@RequestParam(value = "idQuiz") Integer idQuiz) {
        Optional<Quiz> quizOptional = this.quizService.findById(idQuiz);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            quiz.setIsPublish(true);
            this.quizService.insertQuiz(quiz);
            return new Response("Successfully", 200, true, "");
        }
        return new Response("Error", 500, false, "");
    }

    @GetMapping("/search-quiz")
    public ModelAndView searchBySubject(@RequestParam("name") String name, @RequestParam(value = "subject") String subject) {
        ModelAndView modelAndView = new ModelAndView("/home/search-result");
        if (subject == null || subject.trim().equalsIgnoreCase("")) {
            subject = "all";
        }
        List<Quiz> quizList = this.quizService.searchBySubject(new Subject(subject), name);
        modelAndView.addObject("quizList", quizList);
        return modelAndView;
    }
}
