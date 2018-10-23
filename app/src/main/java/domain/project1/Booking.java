package domain.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import java.util.HashMap;

public class Booking extends AppCompatActivity {

    Spinner course,dept,sem,sub;
    Button find;
    String [] courses;
    HashMap<String,String[]> semester = new HashMap<>();
    HashMap<String,String[]> department = new HashMap<>();
    HashMap<String,String[]> subject = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        course = findViewById(R.id.courseSpinner);
        dept = findViewById(R.id.departmentSpinner);
        sem = findViewById(R.id.semesterSpinner);
        sub = findViewById(R.id.subjectSpinner);

        courses = new String[]{"B.Tech", "M.Tech", "MBA"};
        semester.put(courses[0],new String[]{"S1","S2","S3","S4","S5","S6","S7","S8"});
        semester.put(courses[1],new String[]{"S1","S2","S3","S4"});
        semester.put(courses[2],new String[]{"S1","S2","S3","S4"});

        department.put(courses[0],new String[]{"CSE","ME","EC"});
        department.put(courses[1],new String[]{"CSE","EC"});
        department.put(courses[2],new String[]{"Marketing","Management"});
        
        subjectAdd();



    }

    private void subjectAdd() {

        //BTECH

        subject.put("B.Tech_CSE_S1",new String[]{"Maths","Chemistry"});
        subject.put("B.Tech_ME_S1",new String[]{"Maths","Chemistry"});
        subject.put("B.Tech_EC_S1",new String[]{"Maths","Chemistry"});

        subject.put("B.Tech_CSE_S2",new String[]{"Physics","FCPC"});
        subject.put("B.Tech_ME_S2",new String[]{"Physics","BME"});
        subject.put("B.Tech_EC_S2",new String[]{"Physics","Electonics"});

        subject.put("B.Tech_CSE_S3",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_ME_S3",new String[]{"CAD","Maths"});
        subject.put("B.Tech_EC_S3",new String[]{"Signals","Maths"});

        subject.put("B.Tech_CSE_S4",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_ME_S4",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_EC_S4",new String[]{"Data Structure","Maths"});

        subject.put("B.Tech_CSE_S5",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_ME_S5",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_EC_S5",new String[]{"Data Structure","Maths"});

        subject.put("B.Tech_CSE_S6",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_ME_S6",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_EC_S6",new String[]{"Data Structure","Maths"});

        subject.put("B.Tech_CSE_S7",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_ME_S7",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_EC_S7",new String[]{"Data Structure","Maths"});

        subject.put("B.Tech_CSE_S8",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_CSE_S8",new String[]{"Data Structure","Maths"});
        subject.put("B.Tech_CSE_S8",new String[]{"Data Structure","Maths"});

        //BTECH END

        //MTECH

        subject.put("M.Tech_CSE_S1",new String[]{"Mtech1","Mtech2"});
        subject.put("M.Tech_EC_S1",new String[]{"Mtech1","Mtech2"});

        subject.put("M.Tech_CSE_S2",new String[]{"Mtech1","Mtech2"});
        subject.put("M.Tech_EC_S2",new String[]{"Mtech1","Mtech2"});

        subject.put("M.Tech_CSE_S3",new String[]{"Mtech1","Mtech2"});
        subject.put("M.Tech_EC_S3",new String[]{"Mtech1","Mtech2"});

        subject.put("M.Tech_CSE_S4",new String[]{"Mtech1","Mtech2"});
        subject.put("M.Tech_EC_S4",new String[]{"Mtech1","Mtech2"});

        //MTECH END

        //MBA

        subject.put("MBA_Marketing_S1",new String[]{"MBA1","MBA2"});
        subject.put("MBA_Management_S1",new String[]{"MBA1","MBA2"});

        subject.put("MBA_Marketing_S2",new String[]{"MBA1","MBA2"});
        subject.put("MBA_Management_S2",new String[]{"MBA1","MBA2"});

        subject.put("MBA_Marketing_S3",new String[]{"MBA1","MBA2"});
        subject.put("MBA_Management_S3",new String[]{"MBA1","MBA2"});

        subject.put("MBA_Marketing_S4",new String[]{"MBA1","MBA2"});
        subject.put("MBA_Management_S4",new String[]{"MBA1","MBA2"});

        //MBA END
    }
}

