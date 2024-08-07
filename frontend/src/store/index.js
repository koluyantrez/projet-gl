import { createStore } from 'vuex'
/* import fr from '../views/fr.js'
import en from '../views/en.js'
 */

const store = createStore({
  modules: {
    lang: {
      state: {
        curLang: 'fr',
      },
      mutations: {
        setLang(state, lang) {
          if (lang==='fr'){
            state.curLang = lang;
          } else if (lang==='en'){
            state.curLang = lang;
          }
        },
      },
    },
  },
});

export default store;