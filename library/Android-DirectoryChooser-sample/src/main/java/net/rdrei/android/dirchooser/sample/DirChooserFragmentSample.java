package net.rdrei.android.dirchooser.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;


public class DirChooserFragmentSample extends AppCompatActivity implements DirectoryChooserFragment.OnFragmentInteractionListener {

    private TextView mDirectoryTextView;
    private DirectoryChooserFragment mDialog;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        final DirectoryChooserConfig config = DirectoryChooserConfig.builder().initialDirectory("/").build();
        mDialog = DirectoryChooserFragment.newInstance(config);

        mDirectoryTextView = (TextView) findViewById(R.id.textDirectory);

        findViewById(R.id.btnChoose)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.show(getSupportFragmentManager(), null);
                    }
                });
    }

    @Override
    public void onSelectDirectory(@NonNull final String path) {
        mDirectoryTextView.setText(path);
        mDialog.dismiss();
    }

    @Override
    public void onCancelChooser() {
        mDialog.dismiss();
    }
}
