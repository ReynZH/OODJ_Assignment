import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerFileManager {

    private static final String LECTURER_FILE = "user.txt";
    private static final String ASSESSMENT_TYPE_FILE = "assessment_types.txt";
    private static final String ASSESSMENT_RECORD_FILE = "assessment_records.txt";
    private static final String QUIZ_FILE = "quiz_questions.txt";

    // Load lecturer by ID (CSV: id,username,password,name,gender,email,phone,age,role,leaderId)
    public static Lecturer loadLecturerById(String lecturerId) {
        File f = new File(LECTURER_FILE);
        if (!f.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // skip header
                if (line.startsWith("id,")) continue;

                String[] p = line.split(",", -1); // keep empty cols
                if (p.length < 10) continue;

                String id = p[0].trim();
                String password = p[2].trim();
                String name = p[3].trim();
                String email = p[5].trim();
                String role = p[8].trim();

                if (id.equals(lecturerId) && role.equalsIgnoreCase("lecturer")) {
                    return new Lecturer(id, name, email, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Optional: Load lecturer by username
    public static Lecturer loadLecturerByUsername(String username) {
        File f = new File(LECTURER_FILE);
        if (!f.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("id,")) continue;

                String[] p = line.split(",", -1);
                if (p.length < 10) continue;

                String uname = p[1].trim();
                String password = p[2].trim();
                String name = p[3].trim();
                String email = p[5].trim();
                String role = p[8].trim();
                String id = p[0].trim();

                if (uname.equals(username) && role.equalsIgnoreCase("lecturer")) {
                    return new Lecturer(id, name, email, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update lecturer (only updates password, name, email; keeps other columns unchanged)
    public static void saveOrUpdateLecturer(Lecturer lecturer) {
        File file = new File(LECTURER_FILE);
        List<String> lines = new ArrayList<>();

        try {
            if (!file.exists()) return;

            // read all
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }

            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                // keep header unchanged
                if (line.startsWith("id,")) continue;

                String[] p = line.split(",", -1);
                if (p.length < 10) continue;

                String id = p[0].trim();
                String role = p[8].trim();

                if (id.equals(lecturer.getId()) && role.equalsIgnoreCase("lecturer")) {
                    // update only these columns
                    p[2] = lecturer.getPassword(); // password
                    p[3] = lecturer.getName();     // name
                    p[5] = lecturer.getEmail();    // email

                    lines.set(i, String.join(",", p));
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                System.out.println("Lecturer not found, no update made.");
            }

            // write back
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : lines) pw.println(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAssessmentType(AssessmentType type) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ASSESSMENT_TYPE_FILE, true))) {
            pw.println(type.toLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<AssessmentType> loadAssessmentTypesForLecturer(String lecturerId) {
        List<AssessmentType> list = new ArrayList<>();
        File f = new File(ASSESSMENT_TYPE_FILE);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                AssessmentType t = AssessmentType.fromLine(line);
                if (t != null && t.getLecturerId().equals(lecturerId)) {
                    list.add(t);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveAssessmentRecord(AssessmentRecord record) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ASSESSMENT_RECORD_FILE, true))) {
            pw.println(record.toLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<AssessmentRecord> loadAssessmentRecordsForLecturer(String lecturerId) {
        List<AssessmentRecord> list = new ArrayList<>();
        File f = new File(ASSESSMENT_RECORD_FILE);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                AssessmentRecord r = AssessmentRecord.fromLine(line);
                if (r != null && r.getLecturerId().equals(lecturerId)) {
                    list.add(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // delete one assessment type from the file
    public static void deleteAssessmentType(String lecturerId,
                                           String moduleCode,
                                           String assessmentName) {
        File f = new File(ASSESSMENT_TYPE_FILE);
        if (!f.exists()) return;

        List<String> keptLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                AssessmentType t = AssessmentType.fromLine(line);

                if (t == null ||
                        !t.getLecturerId().equals(lecturerId) ||
                        !t.getModuleCode().equals(moduleCode) ||
                        !t.getAssessmentName().equals(assessmentName)) {
                    keptLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
            for (String l : keptLines) {
                pw.println(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // overwrite all records for ONE assessment (used by Edit & Delete in View Details)
    public static void overwriteAssessmentRecordsForAssessment(
            String lecturerId,
            String moduleCode,
            String assessmentName,
            List<AssessmentRecord> updatedRecords) {

        File f = new File(ASSESSMENT_RECORD_FILE);
        List<AssessmentRecord> all = new ArrayList<>();

        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    AssessmentRecord r = AssessmentRecord.fromLine(line);
                    if (r != null) {
                        all.add(r);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<AssessmentRecord> output = new ArrayList<>();
        for (AssessmentRecord r : all) {
            if (r.getLecturerId().equals(lecturerId)
                    && r.getModuleCode().equals(moduleCode)
                    && r.getAssessmentName().equals(assessmentName)) {
                // skip old ones for this assessment
            } else {
                output.add(r);
            }
        }

        output.addAll(updatedRecords);

        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (AssessmentRecord r : output) {
                pw.println(r.toLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ SAVE quiz question into quiz_questions.txt
    // ===== QUIZ =====
public static void saveQuizQuestion(QuizQuestion q) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(QUIZ_FILE, true))) {
        pw.println(q.toLine());
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static List<QuizQuestion> loadQuizQuestionsForLecturer(String lecturerId) {
    List<QuizQuestion> list = new ArrayList<>();
    File f = new File(QUIZ_FILE);
    if (!f.exists()) return list;

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        String line;
        while ((line = br.readLine()) != null) {
            QuizQuestion q = QuizQuestion.fromLine(line);
            if (q != null && q.getLecturerId().equals(lecturerId)) {
                list.add(q);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return list;
}

public static void deleteQuizQuestion(String lecturerId, String moduleCode, String assessmentName, String questionText) {
    File f = new File(QUIZ_FILE);
    if (!f.exists()) return;

    List<String> keptLines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        String line;
        while ((line = br.readLine()) != null) {
            QuizQuestion q = QuizQuestion.fromLine(line);

            if (q == null) {
                keptLines.add(line);
                continue;
            }

            boolean match =
                    q.getLecturerId().equals(lecturerId) &&
                    q.getModuleCode().equals(moduleCode) &&
                    q.getAssessmentName().equals(assessmentName) &&
                    q.getQuestion().equals(questionText);

            if (!match) keptLines.add(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
        return;
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(f, false))) {
        for (String l : keptLines) pw.println(l);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
