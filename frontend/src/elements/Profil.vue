<template>
    <div class="info">
        <p>{{ mail}}</p> <!--data.mail-->
        <p>{{ matricule}}</p> <!--data.id-->
        <p>{{ loc}}</p> <!--data.adress-->
        <p>{{ nu}}</p> <!--data.tel-->
    </div>
    <div class="w">
        <h1>{{id}}</h1> <!--data.firstName+data.lastName-->
    </div>
    <img :src="src" alt="Profil" class="i"/> <!--:src=data.photo-->
</template>

<script>

export default{
    data: () =>{
        return{

            src: require('../assets/profil.png'),

            /* methods:{
        getAllData(){
            Personnel.getAllData().then((response)=>{this.cours=response.data})
        }
        },
        created(){
            this.getAllData()
        } */
        }
    }
}

</script>

<style scoped>
.w{
    position: absolute;
    left: 110px;
    top: 70px;
}

.i{
    position: absolute;
    left: 90px;
    bottom: 350px;
    width: 300px;
    height: 300px;
}

.info{
    position: absolute;
    left: 530px;
    bottom: 300px;
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
    font-size: 100px;
}

</style>
