package ir.unicoce.doctorMahmoud.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import ir.unicoce.doctorMahmoud.R;

public class CommonQuestionsActivity extends AppCompatActivity {

    TextView bottomDialogTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             // TODO : refresh
            }
        });
    }


    private void showContent(String title , String content) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.bottom_dialog_view, null);

        bottomDialogTxt = (TextView) customView.findViewById(R.id.custom_text_view);
        bottomDialogTxt.setText(content);

        new BottomDialog.Builder(this)
                .setCustomView(customView)
                .setTitle(title)
                .show();
    }

}
