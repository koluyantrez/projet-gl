<template>
    <TopStudentPAE/>
</template>
<script>
import ItemCours from '../../elements/ItemCours.vue';
import TopStudentPAE from "@/elements/PAE/TopStudentPAE.vue";

//import Student from '../../model/Student.java';
export default {
    components: {TopStudentPAE, ItemCours},

    /* methods:{
        getCours(){
            Student.getCoursById().then((response)=>{this.cours=response.data})
        }
    },
    created(){
        this.getCoursById()
    } */
}

</script>
<style scoped>
.container {
    position: absolute;
    width: 100%;
    height: 54rem;
    bottom: 0.1rem;
    left: -0.42rem;
    overflow: auto;
    z-index: 1;
    /*border: 3px solid rgb(6, 148, 37); /* Bordure de la zone conteneur */
}



.place {
    position: absolute;
    top: 1rem;
    left: 10%;
}
</style>
