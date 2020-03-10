package e.utilizador.around;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class CreditosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Créditos");
        if(CheckFragment.getInstance().fragmento != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CheckFragment.getInstance().fragmento).commit();
        }
        return inflater.inflate(R.layout.fragment_creditos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
