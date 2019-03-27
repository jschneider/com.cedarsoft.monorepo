# Project set up

Init project and install webpack  
```
npm init -y
npm install webpack webpack-cli --save-dev
```

Edit `package.json`

Install typescript locally  
```npm i typescript --save-dev```

Install the typescript loader locally  
```npm install --save-dev ts-loader```

Initialize typescript project  
```tsc --init```

Adjust `tsconfig.json`  

Install tslint locally   
```npm i tslint --save-dev```

Init tslint  
```npx tslint --init```

Install clean-webpack-plugin to clean dist folder before every build   
```npm install --save-dev clean-webpack-plugin```

Install html-webpack-plugin to create an index.html file  
```npm install html-webpack-plugin```

Create or adjust `webpack.config.js` manually

When installing third party libraries from npm, it is important to remember to install the typing definition for that library. 
These definitions can be found at https://microsoft.github.io/TypeSearch/.  
For example if we want to install lodash we can run the following command to get the typings for it:  
```npm install --save-dev @types/lodash```

SVG Support  
See `./src/custom.d.ts` and its comments.

# Build the project
`npm run build`



