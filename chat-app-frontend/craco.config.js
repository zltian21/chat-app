// eslint-disable-next-line @typescript-eslint/no-var-requires
const CracoLessPlugin = require('craco-less');

module.exports = {
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            modifyVars: {
              '@primary-color': 'rgb(93, 52, 139)',
              '@error-color': '#eb4d4b'
            },
            javascriptEnabled: true
          }
        }
      }
    }
  ]
};
