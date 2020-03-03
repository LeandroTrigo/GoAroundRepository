package e.utilizador.around;

import androidx.fragment.app.Fragment;

public class CheckFragment {

        private static CheckFragment instance = null;
        private CheckFragment() {}

        Fragment fragmento = null;

        public static CheckFragment getInstance() {
            if(instance == null)
                instance = new CheckFragment();
            return instance;
        }

    }

