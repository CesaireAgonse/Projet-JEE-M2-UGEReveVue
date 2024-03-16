 <template>
  <CodeForm v-if="isCodeModalVisible" @close-modal="hideCodeModal" @refresh-filter="filter" :isCodeModalVisible="isCodeModalVisible"></CodeForm>
  <LoginForm v-if="isLoginModalVisible" @close-modal="hideLoginModal" :isLoginModalVisible="isLoginModalVisible" @connect="connect"/>
  <SignupForm v-if="isSignupModalVisible" @close-modal="hideSignupModal" :isSignupModalVisible="isSignupModalVisible" @connect="connect"/>
  <HomeVisual @show-login-modal="showLoginModal" :isLoginModalVisible="isLoginModalVisible"
            @show-signup-modal="showSignupModal" :isSignupModalVisible="isSignupModalVisible"
            @show-code-modal="showCodeModal" :isCodeModalVisible="isCodeModalVisible"
            @disconnect="disconnect"
              @query="handleQuery"
              @sortBy="handleSortBy"
              @pageNumber="handlePageNumber"
              @refresh="filter"
            :isLogged="isLogged" :posts="posts" :totalPage="totalPage" :sortBy="sortBy" :search="q" :photo="photo"/>
</template>

<script>

import LoginForm from "@/components/LoginForm.vue";
import SignupForm from "@/components/SignupForm.vue";
import HomeVisual from "@/visuals/HomeVisual.vue";
import CodeForm from "@/components/CodeForm.vue";
import {authenticationService} from '@/services/authentication.service'
import {codeService} from "@/services/code.service";
import {userService} from "@/services/user.service";
export default {
  mounted() {
    document.title = "Home"
    this.filter()
    this.loadPhoto()
  },
  components: {
    CodeForm,
    HomeVisual,
    SignupForm,
    LoginForm,
  },
  data() {
    return {
      isLogged: authenticationService.isLogged(),
      isLoginModalVisible: false,
      isSignupModalVisible: false,
      isCodeModalVisible: false,
      posts:null,
      sortBy:"",
      q:"",
      pageNumber:0,
      totalPage:0,
      photo:null,
    };
  },
  methods: {
    filter(){
      codeService.filter(this.sortBy, this.q, this.pageNumber).then(res => {
        this.posts = res.data.codes
        this.sortBy = res.data.sortBy
        this.q = res.data.q
        this.pageNumber = res.data.pageNumber
        this.totalPage = res.data.maxPageNumber
      })
    },
    connect(){
      this.loadPhoto()
      this.isLogged = true
      this.filter()
    },
    disconnect(){
      this.photo = null
      this.isLogged = false
      this.filter()
    },
    showLoginModal() {
      this.isLoginModalVisible = true
    },
    hideLoginModal() {
      this.isLoginModalVisible = false
    },
    showSignupModal() {
      this.isSignupModalVisible = true
    },
    hideSignupModal() {
      this.isSignupModalVisible = false
    },
    showCodeModal() {
      this.isCodeModalVisible = true
    },
    hideCodeModal() {
      this.isCodeModalVisible = false
    },
    handleQuery(query){
      this.q = query
      this.filter()
    },
    handleSortBy(sortBy){
      this.sortBy = sortBy
      this.filter()
    },
    handlePageNumber(pageNumber){
      this.pageNumber = pageNumber
      this.filter()
    },
    loadPhoto(){
      if (authenticationService.isLogged()){
        userService.user(authenticationService.getAuth().username).then(res => {
          if (res.data.profilePhoto != null){
            this.photo = "data:image/jpg;base64," + res.data.profilePhoto
          }
        }).catch(err => console.log(err))
      }
    }
  }
};
</script>
