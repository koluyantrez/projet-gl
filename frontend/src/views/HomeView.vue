<template>
  <div class="home">
    <FirstPage/>
  </div>
</template>

<script>
// @ is an alias to /src
import FirstPage from '@/components/everyone/FirstPage.vue';

export default {
  name: 'HomeView',
  components: {
    FirstPage
  }
}
</script>
