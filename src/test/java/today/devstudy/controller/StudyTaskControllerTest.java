package today.devstudy.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class StudyTaskControllerTest {
    private Logger log = (Logger) LoggerFactory.getLogger(StudyTaskControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Test
    void startStudyTask() {
    }

    @Test
    void endStudyTask() {
    }

    @Test
    void findStudyTask() {
    }
}
