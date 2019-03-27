const path = require('path');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  entry: './src/index.ts',
  devtool: 'inline-source-map',
  mode: 'development',
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: 'ts-loader',
        exclude: /node_modules/
      }
    ]
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js']
  },
  plugins: [
    new CleanWebpackPlugin(),
    new HtmlWebpackPlugin({
      hash: false,
      content: 'Sample content injected via HtmlWebpackPlugin',
      template: './src/index.html',
      filename: 'index.html'
    })
  ]
  ,
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist')
  }
};