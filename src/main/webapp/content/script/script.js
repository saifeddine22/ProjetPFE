$(document).ready(function () {
  alert('ready!');
  var map = new ol.Map({
    layers: [],
    target: 'map',
    controls: ol.control.defaults({
      attributionOptions: {
        collapsible: false,
      },
    }),
    view: new ol.View({
      center: [-10.25838565457947, 28.438727490809264],
      zoom: 5,
      projection: 'EPSG:4326',
    }),
  });
  map.addLayer(
    new ol.layer.Tile({
      visible: true,
      preload: Infinity,
      source: new ol.source.XYZ({
        url: 'http://{1-4}.base.maps.cit.api.here.com/maptile/2.1/maptile/newest/normal.day/{z}/{x}/{y}/256/png8?app_id=xWVIueSv6JL0aJ5xqTxb&app_code=djPZyynKsbTjIUDOBcHZ2g',
        crossOrigin: 'anonymous',
      }),
    })
  );
  alert('teeeeesssstttt');
});
