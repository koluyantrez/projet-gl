import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'



const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/guest',
    name: 'guest',
    component: () => import(/* webpackChunkName: "guest" */ '../components/everyone/HomeGuest.vue')
  },
  {
    path: '/membre',
    name: 'membre',
    component: () => import(/* webpackChunkName: "membre" */ '../components/everyone/HomeGuest.vue')
  },
  {
    path: '/prof',
    name: 'prof',
    props : true,
    component: () => import(/* webpackChunkName: "prof" */ '../components/everyone/HomeProf.vue')
  },
  {
    path: '/ins',
    name: 'ins',
    component: () => import(/* webpackChunkName: "ins" */ '../components/everyone/HomeInscr.vue')
  },
  {
    path: '/sec',
    name: 'sec',
    component: () => import(/* webpackChunkName: "prof" */ '../components/everyone/HomeSec.vue')
  },
  {
    path: '/student',
    name: 'student',
    props :true,
    component: () => import(/* webpackChunkName: "student" */ '../components/everyone/HomeStudent.vue'),
  },
  {
    path: '/prof/profil',
    name: 'pprofil',
    component: () => import(/* webpackChunkName: "pprofil" */ '../components/everyone/ProfilProf.vue')
  },
  {
    path: '/sec/profil',
    name: 'seprofil',
    component: () => import(/* webpackChunkName: "seprofil" */ '../components/everyone/ProfilSec.vue')
  },
  {
    path: '/ins/profil',
    name: 'iprofil',
    component: () => import(/* webpackChunkName: "iprofil" */ '../components/everyone/ProfilSec.vue')
  },
  {
    path: '/student/profil',
    name: 'sprofil',
    component: () => import(/* webpackChunkName: "sprofil" */ '../components/everyone/ProfilStudent.vue')
  },
  {
    path: '/membre/profil',
    name: 'mprofil',
    component: () => import(/* webpackChunkName: "mprofil" */ '../components/everyone/ProfilMembre.vue')
  },
  {
    path: '/prof/cours',
    name: 'pcours',
    component: () => import(/* webpackChunkName: "pcours" */ '../components/everyone/CoursProf.vue')
  },
  {
    path: '/sec/cours',
    name: 'secours',
    component: () => import(/* webpackChunkName: "secours" */ '../components/everyone/CoursSec.vue')
  },
  {
    path: '/student/cours',
    name: 'scours',
    component: () => import(/* webpackChunkName: "scours" */ '../components/everyone/CoursStudent.vue')
  },
  {
    path: '/guest/cours',
    name: 'gcours',
    component: () => import(/* webpackChunkName: "gcours" */ '../components/everyone/CoursGuest.vue')
  },
  {
    path: '/membre/cours',
    name: 'mcours',
    component: () => import(/* webpackChunkName: "mcours" */ '../components/everyone/CoursGuest.vue')
  },
  {
    path: '/guest/signup',
    name: 'signup',
    component: () => import(/* webpackChunkName: "signup" */ '../components/everyone/SignUp.vue')
  },
  {
    path: '/ins/signup',
    name: 'hired',
    component: () => import(/* webpackChunkName: "hired" */ '../components/everyone/InsSignUp.vue')
  },
  {
    path: '/addNewCours',
    name: 'newCours',
    component: () => import(/* webpackChunkName: "newCours" */ '@/components/extensionOussama/AddNewCours.vue')
  },
  {
    path: '/roomReservationRequest',
    name: 'roomReservation',
    component: () => import(/* webpackChunkName: "roomReservation" */ '@/components/extensionOussama/RoomReservation.vue')
  },
  {
    path: '/detailsCourse',
    name: 'DetailsCours',
    component: () => import(/* webpackChunkName: "DetailsCours" */ '@/components/extensionOussama/DetailsCourse.vue')
  },
  {
    path: '/course-Section/:cours',
    name: 'courseSection',
    component: () => import(/* webpackChunkName: "courseSection" */ '../components/everyone/CoursStudent.vue')
  },
  {
    path: '/inscription',
    name: 'inscription',
    component: () => import(/* webpackChunkName: "inscription" */ '../components/everyone/HomeInscr.vue')
  },
  {
    path: '/student/PAE',
    name: 'sPAE',
    component: () => import(/* webpackChunkName: "sPAE" */ '../components/Estebane/StudentPAEHomePage.vue')
  },
  {
    path:'/student/PAE/actual',
    name:'sPAEactual',
    component: () => import(/* webpackChunkName: "sPAEactual" */ '../components/Estebane/StudentPAEActualPage.vue')
  },

  {
    path: '/dospii',
    name: 'dhome',
    component: () => import(/* webpackChunkName: "dhome" */ '../components/ExtensionAina/compo/FirstAina.vue')
  },
  {
    path: '/dospii/profil',
    name: 'dprofilg',
    component: () => import(/* webpackChunkName: "dprofilg" */ '../components/ExtensionAina/compo/ProfilGuestDospii.vue')
  },
  {
    path: '/dospii/myprofil',
    name: 'dprofil',
    component: () => import(/* webpackChunkName: "dprofil" */ '../components/ExtensionAina/compo/ProfilAina.vue')
  },
  {
    path: '/roomsManagement',
    name : 'roomManagement',
    component: () =>import ('../components/extensionOussama/RoomManagement.vue')
  },
  {
    path: '/dospii/posted',
    name: 'dposted',
    component: () => import(/* webpackChunkName: "dposted" */ '../components/ExtensionAina/compo/Posted.vue')
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})


export default router
