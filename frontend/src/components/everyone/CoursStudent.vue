<template>
    <TopStudent/>
    <div class="w">
        <h1>{{name}}</h1> <!--co.code+co.name-->
    </div>
    <div class="info">
        <h2>Titulaire : {{ prof }}</h2> <!--cours.prof-->
        <h3>Assistant<li v-for="(item, index) in assi" :key="index">{{ item.a }}</li></h3>
                     <!--<li v-for="co in cours" v-bind:key="co.assistant">{{ co.assistant }}</li>-->
    </div>
</template>
<script>
import TopStudent from '../../elements/TopStudent.vue';
//import Cours from '../../model/Cours.java';
export default{
    components: {TopStudent},
    data: () => {
        return{
            name: 'S-INFO-810 Quantum Computing',
            prof: 'Seweryn Dynerowicz',
            assi: [
                {a: 'Julien Ladeuze'},
                {a: 'Maxime Bazin'},
                {a: 'Kacem Barkani'}
        ],
        }
    }
    /* methods:{
        getAllData(){
            Cours.getAllData().then((response)=>{this.cours=response.data})
        }
        },
        created(){
            this.getAllData()
        } */
}
</script>
<style scoped>
.w{
    position: absolute;
    left: 110px;
    top: 70px;
}

.info{
    position: absolute;
    left: 120px;
    top: 225px;
    color: rgb(158, 11, 23);
    font-family: Roboto,sans-serif;
    font-size: 43px;
}

</style>
<style>
h1{
    color: rgb(158, 11, 23);
    font-family: Roboto,sans-serif;
    font-weight: 450;
    font-size: 90px;
}
h2{
    color: rgb(158, 11, 23);
    font-family: Roboto,sans-serif;
    font-weight: 450;
    font-size: 30px;
}
h3{
    color: rgb(158, 11, 23);
    font-family: Roboto,sans-serif;
    font-weight: 300;
    font-size: 30px;
}
</style>