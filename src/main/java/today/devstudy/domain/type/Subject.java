package today.devstudy.domain.type;

import today.devstudy.domain.studyTask.StudyTask;

public enum Subject {
    /**
     * todo
     * 주제 종류를 추가할 예정
     */
    C,JAVA,CPP,PYTHON,SPRING,KOTLIN,ALGORITHM;

    public static StudyTask from(Subject subject){
        return new StudyTask(subject);
    }
}
