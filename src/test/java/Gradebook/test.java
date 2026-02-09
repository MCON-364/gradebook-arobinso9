package Gradebook;
import edu.course.gradebook.Gradebook;
import edu.course.gradebook.Gradebook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class test {
    private Gradebook gradebook;

    @BeforeEach
    void setUp() {
        gradebook = new Gradebook();
    }
    @Test
    void addStudent_ifDuplicateStudentThenReturnFalse() {
        assertTrue(gradebook.addStudent("Aviva"));
        assertFalse(gradebook.addStudent("Aviva"));
    }
    @Test
    void addStudent_createsEmptyGradeList() {
        assertTrue(gradebook.addStudent("Aviva"));
        var grades = gradebook.findStudentGrades("Aviva");
        assertTrue(grades.isPresent());
        assertTrue(grades.get().isEmpty());
    }

    @Test
    void addGrade_ifStudentNotThereReturnFalse() {
        assertFalse(gradebook.addGrade("Aviva", 100));
    }
    @Test
    void addGrade_ifStudentExistsThenAddGrade() {
        gradebook.addStudent("Aviva");
        assertTrue(gradebook.addGrade("Aviva", 105));
        var grades = gradebook.findStudentGrades("Aviva");
        assertTrue(grades.isPresent());
        assertEquals(1, grades.get().size());
        assertEquals(105, grades.get().getFirst());

    }
    @Test
    void addGrade_canAddMultipleGradesPerStudent(){
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 105);
        gradebook.addGrade("Aviva", 102);
        gradebook.addGrade("Aviva", 103);
        var grades = gradebook.findStudentGrades("Aviva");
        assertTrue(grades.isPresent());
        assertEquals(3, grades.get().size());
        assertEquals(105, grades.get().get(0));
        assertEquals(102, grades.get().get(1));
        assertEquals(103, grades.get().get(2));
    }
    @Test
    void removeStudent_whenStudentExistsTheyAreRemoved(){
        gradebook.addStudent("Aviva");
        var grades = gradebook.findStudentGrades("Aviva");
        assertTrue(gradebook.removeStudent("Aviva"));
        assertTrue(grades.isEmpty());
    }
    @Test
    void removeGrade_whenStudentDoesNotExistTheyAreNotRemoved(){
        gradebook.addStudent("Aviva");
        assertFalse(gradebook.removeStudent("Aviva"));
    }
    @Test
    void averageFor_inValidStudentReturnsEmpty(){
        assertTrue(gradebook.averageFor("Aviva").isEmpty());
    }
    @Test
    void averageFor_ValidStudentWithNoGradeListReturnsEmpty(){
        gradebook.addStudent("Aviva");
        var grades = gradebook.findStudentGrades("Aviva");
        assertTrue(grades.isEmpty());
    }
    @Test
    void averageFor_ValidStudentWithMultipleGradeListsReturnsAverage(){
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 110);
        gradebook.addGrade("Aviva", 100);
        gradebook.addGrade("Aviva", 90);
        var grades = gradebook.findStudentGrades("Aviva");
        assertTrue(grades.isPresent());
        assertEquals(3, grades.get().size());
        var avg = gradebook.averageFor("Aviva");
        assertTrue(avg.isPresent());
        assertEquals(100.0, avg.get());
    }
    @Test
    void letterGradeFor_InvalidStudentReturnsEmpty(){
        assertTrue(gradebook.letterGradeFor("Aviva").isEmpty());
    }
    @Test
    void letterGradeFor_ValidStudentWithNoGradeListReturnsEmpty(){
        gradebook.addStudent("Aviva");
        var grades = gradebook.findStudentGrades("Aviva");
        assertTrue(grades.isEmpty());
    }
    @Test
    void letterGradeFor_DefaultWorksCorrectly(){
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 32);
        var letterGrade= gradebook.letterGradeFor("Aviva");
        assertTrue(letterGrade.isPresent());
        assertEquals("F", letterGrade.get());
    }
    @Test
    void letterGradeFor_returnsA_from90to100() {
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 90);
        gradebook.addGrade("Alice", 100);

        var letter = gradebook.letterGradeFor("Aviva");
        assertTrue(letter.isPresent());
        assertEquals("A", letter.get());
    }
    @Test
    void letterGradeFor_returnsB_from80to89() {
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 80);
        gradebook.addGrade("Aviva", 89);

        var letter = gradebook.letterGradeFor("Aviva");
        assertTrue(letter.isPresent());
        assertEquals("B", letter.get());
    }
    @Test
    void letterGradeFor_returnsC_from70to79() {
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 70);
        gradebook.addGrade("Aviva", 79);

        var letter = gradebook.letterGradeFor("Aviva");
        assertTrue(letter.isPresent());
        assertEquals("C", letter.get());
    }
    @Test
    void letterGradeFor_returnsD_from60to69() {
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 60);
        gradebook.addGrade("Aviva", 69);

        var letter = gradebook.letterGradeFor("Aviva");
        assertTrue(letter.isPresent());
        assertEquals("D", letter.get());
    }
    @Test
    void classAverage_IfNoStudentsReturnEmpty(){
        assertTrue(gradebook.classAverage().isEmpty());
    }
    @Test
    void classAverage_IfStudentsButNoGradeListReturnsEmpty(){
        gradebook.addStudent("Aviva");
        gradebook.addStudent("CTK");
        assertTrue(gradebook.classAverage().isEmpty());
    }
    @Test
    void classAverage_IfStudentsWithGradeListReturnsAverage(){
        gradebook.addStudent("Aviva");
        gradebook.addStudent("CTK");
        gradebook.addGrade("Aviva", 90);
        gradebook.addGrade("Aviva", 90);
        gradebook.addGrade("CTK", 100);
        gradebook.addGrade("Aviva", 100);
        var average = gradebook.classAverage();
        assertTrue(average.isPresent());
        assertEquals(100, average.get());
    }
    @Test
    void classAverage_ignoresStudentsWithNoGrades() {
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 90);
        gradebook.addStudent("CTK"); // No grades
        var avg = gradebook.classAverage();
        assertTrue(avg.isPresent());
        assertEquals(90.0, avg.get());
    }
    @Test
    void undo_WhenNothingToUndoThenReturnFalse() {
        assertFalse(gradebook.undo());
    }
    @Test
    void undo_AddGrade() {
        gradebook.addStudent("Aviva");
        gradebook.addGrade("Aviva", 100);
        var gradesBefore = gradebook.findStudentGrades("Aviva");
        assertEquals(1, gradesBefore.get().size());
        assertTrue(gradebook.undo());
        var gradesAfter = gradebook.findStudentGrades("Aviva");
        assertTrue(gradesAfter.isPresent());
        assertEquals(0, gradesAfter.get().size());
    }
    @Test
    void undo_RemoveStudent(){
        gradebook.addStudent("Aviva");
        var gradesBefore = gradebook.findStudentGrades("Aviva");
        assertTrue(gradesBefore.isPresent());
        assertTrue(gradebook.undo());
        var gradesAfter = gradebook.findStudentGrades("Aviva");
        assertFalse(gradesAfter.isPresent());
    }
    @Test
    void recentLog_IfItemsToBeSeenIsGreaterThanTotalItemsReturnFullOgList(){
        gradebook.addStudent("Aviva");
        gradebook.addStudent("CTK");
        gradebook.addGrade("Aviva", 89);
        var log = gradebook.recentLog(100);
        assertEquals(3, log.size());
    }
    @Test
    void recentLog_BeforeAnyActionIsDoneReturnsEmptyList() {
        var log = gradebook.recentLog(10);
        //ensuring an object was actually returned- and not null
        assertNotNull(log);
        // once we established an object is returned, now we check to make sure its empty
        assertTrue(log.isEmpty());
    }
    @Test
    void recentLog_ReturnsAmountOfItemsRequested(){
        gradebook.addStudent("Aviva");
        for (int i = 0; i < 10; i++) {
            gradebook.addGrade("Aviva", 90);
        }
        var log = gradebook.recentLog(5);
        assertEquals(5, log.size());
    }



}
