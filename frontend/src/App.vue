<template>
    <div id="app">

        <Navbar/>

        <!--<HelloWorld msg="Welcome to Your Vue.js App"/>-->

        <!--<Home/>-->

    </div>
</template>

<script>
    import HelloWorld from './components/HelloWorld.vue'
    import Home from "./components/Home";
    import Navbar from "./components/Navbar";

    export default {
        name: 'app',
        components: {
            Navbar,
            Home,
            HelloWorld
        }
    }
</script>

<style lang="sass">

    @import "../node_modules/bulma/bulma.sass"

</style>
