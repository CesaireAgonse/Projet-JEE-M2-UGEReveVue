<template>
  <div class="post"  v-if="post.unitContent !== null && post.unitContent !== ''">
    <pre class="post-code language-java"><code>{{ post.unitContent }}</code></pre>
    <div class="row">
      <div>
        <p>Total: {{post.testResultsInformation.testsTotalCount}}</p>
        <p><i class="fa-solid fa-check" style="color: #63E6BE;"></i> {{post.testResultsInformation.testsSucceededCount}}</p>
        <p><i class="fa-solid fa-xmark" style="color: #f44e4e;"></i> {{post.testResultsInformation.testsFailedCount}}</p>
        <p><i class="fa-solid fa-hourglass-end" style="color: #74C0FC;"></i> {{post.testResultsInformation.testsTotalTime}}</p>
      </div>
      <div class="failures">
        <p>Echecs: </p>
        <pre>{{post.testResultsInformation.failures}}</pre>
      </div>
    </div>
  </div>
</template>

<script>
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

library.add(fas, far, fab)
dom.watch();
export default {
  props: {
    post: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>
.post {
  background-color: #282828;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  padding: 20px;
  margin: 20px;
  width: calc(33.3333% - 40px); /* 1/3 de la largeur de l'écran moins la marge et le padding */
}
.post-code {
  background-color: #0c0808;
  border-radius: 8px;
  padding: 15px;
  font-family: 'Courier New', Courier, monospace;
  white-space: pre-wrap;
  text-align: left;
}
.row {
  display: flex;
  flex-direction: row;
}

.failures{
  text-align: left;
  padding-left: 50px;
}


</style>
