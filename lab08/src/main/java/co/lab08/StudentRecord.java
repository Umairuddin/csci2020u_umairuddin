package co.lab08;

public class StudentRecord {
    public enum LetterGrade{
        A,B,C,D,F;
    }

    private String StudentID;
    private float Midterm;
    private float Assignments;
    private float FinalExam;
    private float FinalMark;
    private LetterGrade Grade;

    public StudentRecord(String StudentID, float Midterm, float Assignments, float FinalExam){
        this.StudentID = StudentID;
        this.Midterm = Midterm;
        this.Assignments = Assignments;
        this.FinalExam = FinalExam;
        float finalGrade = (float)((this.Midterm*0.3) + (this.Assignments*0.3) + (this.FinalExam*0.4));
        this.FinalMark = finalGrade;
        if(finalGrade <= 49)
            this.Grade = LetterGrade.F;
        else if(finalGrade <= 59)
            this.Grade = LetterGrade.D;
        else if(finalGrade <= 69)
            this.Grade = LetterGrade.C;
        else if(finalGrade <= 79)
            this.Grade = LetterGrade.B;
        else
            this.Grade = LetterGrade.A;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public float getMidterm() {
        return Midterm;
    }

    public void setMidterm(float midterm) {
        Midterm = midterm;
    }

    public float getAssignments() {
        return Assignments;
    }

    public void setAssignments(float assignments) {
        Assignments = assignments;
    }

    public float getFinalExam() {
        return FinalExam;
    }

    public void setFinalExam(float finalExam) {
        FinalExam = finalExam;
    }

    public float getFinalMark() {
        return FinalMark;
    }

    public void setFinalMark(float finalMark) {
        FinalMark = finalMark;
    }

    public LetterGrade getGrade() {
        return Grade;
    }

    public void setGrade(LetterGrade grade) {
        Grade = grade;
    }
}
