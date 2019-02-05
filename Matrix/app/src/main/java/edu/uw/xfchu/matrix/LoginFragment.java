package edu.uw.xfchu.matrix;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private View loginLayout;
    private View logoutLayout;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Static function, create loginFragment instance
     * @return new instance of accident fragment
     */
    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginLayout = view.findViewById(R.id.loginLayout);
        logoutLayout = view.findViewById(R.id.logoutLayout);
        showLayout();
        return view;
    }

    private void showLayout() {
        if (Config.username == null) {
            logoutLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        } else {
            logoutLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility((View.GONE));
        }
    }
}
