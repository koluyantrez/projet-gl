<template>
  <div>
    <div class="popup">
      <div class="inner">
        <slots/>
        <center>
          <form class="file-upload-form">
            <label for="file" class="file-upload-label">
              <div class="file-upload-design">
                <svg viewBox="0 0 640 512" height="1em">
                  <path
                      d="M144 480C64.5 480 0 415.5 0 336c0-62.8 40.2-116.2 96.2-135.9c-.1-2.7-.2-5.4-.2-8.1c0-88.4 71.6-160 160-160c59.3 0 111 32.2 138.7 80.2C409.9 102 428.3 96 448 96c53 0 96 43 96 96c0 12.2-2.3 23.8-6.4 34.6C596 238.4 640 290.1 640 352c0 70.7-57.3 128-128 128H144zm79-217c-9.4 9.4-9.4 24.6 0 33.9s24.6 9.4 33.9 0l39-39V392c0 13.3 10.7 24 24 24s24-10.7 24-24V257.9l39 39c9.4 9.4 24.6 9.4 33.9 0s9.4-24.6 0-33.9l-80-80c-9.4-9.4-24.6-9.4-33.9 0l-80 80z"
                  ></path>
                </svg>
                <p>{{cLang.Drop.see}}</p>

                <span class="browse-button">{{cLang.Drop.explo}}</span>
              </div>
              <input id="file" type="file" />
            </label>
          </form>

          <ItemAdd class="close" :word="cLang.Drop.back" @click="ToPicPopup()"/>
        </center>
      </div>
    </div>
    <div class="uploaded-image" v-if="uploadedFile">
      <img :src="uploadedFile" alt="Uploaded Image" />
    </div>
  </div>

</template>

<script>
import ItemAdd from '../elements/ItemAdd.vue';
import { useStore } from 'vuex';
import { computed, watch, ref } from 'vue';
import fr from '../views/fr.js';
import en from '../views/en.js';
export default{
  components: {ItemAdd},
  props: ['ToPicPopup'],

  setup(){

    const store = useStore();
    const idLa = computed(() => store.state.lang.curLang);
    const cLang = ref(idLa.value === 'fr' ? fr : en);

    watch(idLa, (newLang) => {
      cLang.value = newLang === 'fr' ? fr : en;
    });

    const uploadedFile = ref(null);
    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      uploadedFile.value = URL.createObjectURL(file);
    };

    return{
      cLang,
      uploadedFile,
      handleFileUpload
    }
  }
}

</script>
<style scoped>
.popup{
  position: fixed;
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
  z-index: 99;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;

  .inner{
    position: relative;
    background: rgb(255, 255, 255);
    padding: 39px;
    border-radius: 10%;
  }
}
.po{
  font-size: 30px;
  color: rgb(134, 10, 10);
  font-family: Roboto,sans-serif;
}

.file-upload-form {
  width: fit-content;
  height: fit-content;
  display: flex;
  align-items: center;
  justify-content: center;
}
.file-upload-label input {
  display: none;
}
.file-upload-label svg {
  height: 50px;
  fill: rgb(255, 0, 43);
  margin-bottom: 20px;
}
.file-upload-label {
  cursor: pointer;
  background-color: rgb(66, 3, 3);
  padding: 30px 70px;
  border-radius: 40px;
  border: 2px dashed rgb(255, 0, 43);
  box-shadow: 0px 0px 200px -50px rgba(0, 0, 0, 0.719);
}
.file-upload-design {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 5px;
}
.browse-button {
  background-color: rgb(240, 153, 153);
  padding: 5px 15px;
  border-radius: 10px;
  color: rgb(255, 0, 43);
  transition: all 0.3s;
}
.browse-button:hover {
  background-color: rgb(255, 255, 255);
}

/* Styles pour l'image téléchargée */
.uploaded-image {
  position: fixed;
  top: 20px; /* Ajustez ces valeurs pour positionner l'image où vous le souhaitez */
  right: 20px;

}
.uploaded-image img {
  max-width: 100%; /* Assurez-vous que l'image ne dépasse pas de son conteneur */
}
</style>
  