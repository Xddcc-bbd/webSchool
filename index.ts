import { createRouter, createWebHistory } from 'vue-router'
//import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: ()=>import('../views/login.vue'),
    },
	{
	  path: '/home',
	  name: 'home',
	  component: ()=>import('../views/Home.vue'),
	  children: [
		  {
		    path: '/bookoutline',
		    name: 'bookoutline',
		    component: ()=>import('../views/bookOutline.vue'),
		  },
		  {
			path: '/bookmanage',
		    name: 'bookmanage',
		    component: ()=>import('../views/bookManage.vue'),  
		   
		  },
		  {
		     path: '/brmanage',
		    name: 'brmanage',
		    component: ()=>import('../views/brManage.vue'),
		  }
	  ]
	},
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
  ],
})

export default router
