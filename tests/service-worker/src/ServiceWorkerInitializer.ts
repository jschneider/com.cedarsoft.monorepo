export class ServiceWorkerInitializer {

  public init(): void {
    if ('serviceWorker' in navigator && 'SyncManager' in window) {
      navigator.serviceWorker.register('ServiceWorker.js')
        .then(function (reg) {
          // registration worked
          console.log('Registration succeeded. Scope is ' + reg.scope);
          reg.sync.register('update-tile.' + Math.random());
        }).catch(function (error) {
        // registration failed
        console.log('Registration failed with ' + error);
      });
    }
    else {
      console.log('"serviceworker" or "SyncManager" is not supported');
      // TODO
    }
  }
}


new ServiceWorkerInitializer().init();