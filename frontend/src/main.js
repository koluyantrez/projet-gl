import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import 'select2/dist/css/select2.min.css';
import 'select2/dist/js/select2.min.js';
import $ from 'jquery';

const app = createApp(App);

// Register the store and router
app.use(store).use(router);

// Mount the app
app.mount('#app');

// Ensure that Select2 is initialized after the component is mounted
app.mixin({
    mounted() {
        // Initialize Select2 elements
        $(this.$el).find('select').select2();
    }
});