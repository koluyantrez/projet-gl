<template>
    <TopSec/>
    <div class="data">
        <ItemInput :name="cLang.SignUp.ln"/>
        <ItemInput :name="cLang.SignUp.fn"/>
        <ItemInput :name="cLang.SignUp.born"/>
        <ItemInput :name="cLang.SignUp.ad"/>
        <ItemInput :name="cLang.SignUp.city"/>
        <ItemInput :name="cLang.SignUp.phone"/>
        <ItemInput :name="cLang.SignUp.p"/>
        <ItemInput type="password" :name="cLang.SignUp.pw"/>
    </div>
    <ItemAdd class="pic" word="Photo" @click="() => ToPicPopup('buPic')"/>
    <ItemButton class="finish" :name="cLang.SignUp.done"/>
    <input id="file" type="file" style="display: none;" @change="handleFileUpload" />
    
    <div class="uploaded-image" v-if="uploadedFile">
        <img class="see" :src="uploadedFile"  />
    </div>
    
    <DropImg v-if = "AddPic.buPic" :ToPicPopup="() => ToPicPopup('buPic')" />
    </template>
    <script>
    import TopSec from '../../elements/TopSec.vue';
    import ItemInput from '../../elements/ItemInput.vue';
    import ItemButton from '../../elements/ItemButton.vue';
    import ItemAdd from '../../elements/ItemAdd.vue';
    import ItemGrade from '../../elements/ItemGrade.vue';
    import DropImg from '../../popup/DropImg.vue';
    import { useStore } from 'vuex';
    import { computed, watch, ref } from 'vue';
    import fr from '../../views/fr.js';
    import en from '../../views/en.js';
    export default{
        components: { TopSec, ItemInput, ItemButton ,ItemAdd, DropImg,ItemGrade},
        setup(){
    
            const store = useStore();
            const idLa = computed(() => store.state.lang.curLang);
            const cLang = ref(idLa.value === 'fr' ? fr : en);
    
            watch(idLa, (newLang) => {
                cLang.value = newLang === 'fr' ? fr : en;
            });
    
            const AddPic = ref({
                    buPic: false
                });
                const ToPicPopup = (tri)=>{
                    AddPic.value[tri]=!AddPic.value[tri]
                }
    
            const uploadedFile = ref(null);
            const handleFileUpload = (event) => {
            const file = event.target.files[0];
            uploadedFile.value = URL.createObjectURL(file);
            };
    
            return{
                AddPic,
                ToPicPopup,
                uploadedFile,
                handleFileUpload,
    
                cLang,
    
    
            }
                    
        }
    }
    </script>
    <style scoped>
    
    .pic{
        position: absolute;
        top: 8rem;
        left: 45rem;
    
    }
    
    .finish{
        position: absolute;
        bottom: 2rem;
        left: 5rem;
    }
    
    .data{
        position: absolute;
        left: 5rem;
        top: 5rem;
    }
    
    .tmp{
        position: absolute;
        top: 10rem;
        right: 15rem;
    }
    
    .uploaded-image{
        position: absolute;
        bottom: 40rem;
        left: 50rem;
    }   
    
    .see{
        position: absolute;
        width: 25rem;
        height: 25rem;
    }
</style>
