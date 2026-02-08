package edu.course.gradebook;

import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();

    public Optional<List<Integer>> findStudentGrades(String name) {
        return Optional.ofNullable(gradesByStudent.get(name));
    }

    /*
    addStudent(String name)
    Add a student to the gradesByStudent map
    Initialize with an empty ArrayList<Integer>
    Return false if student already exists
    Add a log entry
    Return true on success
     */
    public boolean addStudent(String name) {
        if (!gradesByStudent.containsKey(name)){
            gradesByStudent.put(name, new ArrayList<Integer>());
            activityLog.add("Added Student: " + name);
            return true;
        }
        return false;
    }

    /*
    addGrade(String name, int grade)
    Find the student's grade list
    Add the grade to their list
    Push an undo action to undoStack (to remove this grade)
    Add a log entry
    Return false if student not found
    Return true on success
     */
    public boolean addGrade(String name, int grade) {
        if(gradesByStudent.containsKey(name)){
            var grades = gradesByStudent.get(name);
            grades.add(grade);
            //we create a lambda, so if a user wants to undo the grade we just added, we can
            // just undo it- and remove it from the grade list.
            // grades.removeLast() gets us the last thing we just added to the stack.
            // Lambdas are only run if u call them...()-> is the lambda part
            undoStack.push((UNDO) -> grades.removeLast());
            activityLog.add("Added Grade " + grade+ " for " + name);
            return true;
        }
        return false;
    }

    /*
    removeStudent(String name)
    Remove the student from gradesByStudent
    Push an undo action to undoStack (to restore the student)
    Add a log entry
    Return false if student not found
    Return true on success
     */
    public boolean removeStudent(String name) {
        if(gradesByStudent.containsKey(name)){
            var gradeList= gradesByStudent.get(name);
            gradesByStudent.remove(name);
            activityLog.add("Removed Student: " + name);
            undoStack.push((UNDO) -> gradesByStudent.put(name, gradeList));
            return true;
        }
        return false;
    }

    /*
    averageFor(String name)
    Use an enhanced for loop to sum grades
    Calculate and return the average as Optional<Double>
    Return Optional.empty() if no grades exist

    Optional is like a gift box
    Optional.empty() is an empty box. There were no valid results to return. Prevents NPE
    Optional.of(value) is a box with something inside. There was a valid result returned.
    Now isPresent() will return true for Optional.of() and false for Optional.empty()
    and isEmpty() will return false for Optional.of() and true for Optional.empty()
     */
    public Optional<Double> averageFor(String name) {
        if(!gradesByStudent.containsKey(name) || gradesByStudent.get(name).isEmpty())
            return Optional.empty();
        var grades = gradesByStudent.get(name);
        var gradeTtl=0;
        for (var grade: grades){
            gradeTtl+=grade;
        }
        return Optional.of((double) gradeTtl/grades.size());
    }

    /*
    letterGradeFor(String name)
    Get the average for the student
    Use a switch expression with yield to convert average to letter grade:
    90–100 → A
    80–89 → B
    70–79 → C
    60–69 → D
    < 60 → F
    Return Optional<String> with the letter grade
    Return Optional.empty() if student has no grades
     */
    public Optional<String> letterGradeFor(String name) {
        var avgOptional= averageFor(name);
        if(avgOptional.isEmpty())
            return Optional.empty();
        /*
        when we have an optional object its a box sp, we cant use it in calculations unless
        we extract the value of the box! which is why we did avgOptional.get();
        Also since avgOptional is type Optional<Double> when we do get(), a Double obj
        is returned. Not the primitive double. Therefor we cant directly cast it to an int.
        So either label avg as double avg, or when casting we need to do avg.intValue()
        which is a method specifically for Objects.
         */
        var avg= avgOptional.get();
        String grade = switch(avg.intValue()/10){
            case 10,9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            case 6 -> "D";
            default -> {
                yield "F";
            }
        };
        return Optional.of(grade);
    }

    /*
    classAverage()
    Calculate average across all grades for all students
    Use enhanced for loops to iterate over students and their grades
    Return Optional<Double>
    Return Optional.empty() if no grades exist in the system
     */
    public Optional<Double> classAverage() {
        if(gradesByStudent.isEmpty())
            return Optional.empty();
        var gradeTtl=0;
        var countTtl=0;
        // get each list of grades and add them all up
        for(var gradeList: gradesByStudent.values()){
            for(var grade: gradeList){
                gradeTtl+=grade;
                countTtl++;
            }
        }
        if(countTtl==0)
            return Optional.empty();
        return Optional.of((double)gradeTtl/countTtl);
    }

    public boolean undo() {
        throw new UnsupportedOperationException();
    }

    public List<String> recentLog(int maxItems) {
        throw new UnsupportedOperationException();
    }
}
