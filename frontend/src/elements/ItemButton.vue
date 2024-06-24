<template>
    <button @click="cli">{{name}}</button>
</template>

<script>
export default{
    props: ['name','click'],
    methods:{
        cli(){
        }
    }
};
</script>

<style scoped>

    button{
    padding: 0.5em 2em;
    font-size: 40px;
    font-family: roboto, sans-serif;
    box-shadow: 2px 2px 4px rgba(0,0,0,0.4);
    background: rgb(255, 255, 255);
    color: rgb(0, 0, 0);
    border-radius: 10px;
    width: 450px;
    border: transparent;
    margin-right: 2rem;
    margin-bottom: 1rem;
}


button:hover {
background: rgb(2,0,36);
background: linear-gradient(90deg, rgb(255, 0, 34) 0%, rgb(255, 72, 103) 100%);
}

button:active {
    transform: translate(0em, 0.2em);
}
</style>