<link rel="stylesheet" href="css/semantic.min.css" type="text/css" media="all">
<script type="text/javascript" src="javascript/semantic.min.js" ></script>
<html>
    <head>
        <script src="http://www.mapquestapi.com/sdk/js/v7.0.s/mqa.toolkit.js?key=Fmjtd%7Cluub2h0z21%2Cas%3Do5-9utad6"></script>

        <script type="text/javascript">

            /*An example of using the MQA.EventUtil to hook into the window load event and execute defined function
             passed in as the last parameter. You could alternatively create a plain function here and have it
             executed whenever you like (e.g. <body onload="yourfunction">).*/

            MQA.EventUtil.observe(window, 'load', function() {

                /*Create an object for options*/
                var options = {
                    elt: document.getElementById('map'), /*ID of element on the page where you want the map added*/
                    zoom: 10, /*initial zoom level of map*/
                    latLng: {lat: -6.180893, lng: 106.828461}, /*center of map in latitude/longitude*/
                    mtype: 'map',                                /*map type (map)*/
                    zoomOnDoubleClick:true 
                };

                /*Construct an instance of MQA.TileMap with the options object*/
                window.map = new MQA.TileMap(options);
                <%
                if(session.getAttribute("lat1")!=null){                   
                %>
                MQA.withModule('directions', function() {
                    /*Uses the MQA.TileMap.addRoute function (added to the TileMap with the directions module)
                     passing in an array of location objects as the only parameter.*/
                    map.addRoute([
                        {latLng: {lat: <%=session.getAttribute("lat1")%>, lng: <%=session.getAttribute("long1")%>}},
                        {latLng: {lat: <%=session.getAttribute("lat2")%>, lng: <%=session.getAttribute("long2")%>}}
                    ]);
                });
                <%}%>
                MQA.withModule('largezoom', function() {

                    map.addControl(
                            new MQA.LargeZoom(),
                            new MQA.MapCornerPlacement(MQA.MapCorner.TOP_LEFT, new MQA.Size(5, 5))
                            );

                });
            });

        </script>
    </head>

    <body class="ui teal inverted segment">
        <div class="ui grid">
            <div class="four wide column">
                <form action = "/TwitterPrediction/LocationEstimation" method = "post">
                    <div class="ui inverted form segment">
                        <div class="field">
                            <label>From</label>
                            <div class="ui left labeled icon input">
                                <input placeholder="From" type = "text" name = "from"  />
                                <div class="ui corner label">
                                    <i class="icon asterisk"></i>
                                </div>
                            </div>
                        </div>
                        <div class="field">
                            <label>To</label>
                            <div class="ui left labeled icon input">
                                <input placeholder="To" type = "text" name = "to"  />
                                <div class="ui corner label">
                                    <i class="icon asterisk"></i>
                                </div>
                            </div>
                        </div>
                        <div class="ui error message">
                            <div class="header">We noticed some issues</div>
                        </div>
                        <input type = "submit" value = "Submit" class="ui blue submit button"/>
                    </div>
                </form>  
            </div>
            
            <div class="ten wide column">
                <% if(session.getAttribute("lat1")!=null) {%>
                <div class="ui green inverted segment">
                    <p>From : <%=session.getAttribute("from")%></p>
                    <p>To : <%=session.getAttribute("to")%></p>
<!--                    <p>Lat1 : <%=session.getAttribute("lat1")%></p>
                    <p>Long1 : <%=session.getAttribute("long1")%></p>
                    <p>Lat2 : <%=session.getAttribute("lat2")%></p>
                    <p>Long2 : <%=session.getAttribute("long2")%></p>-->
                </div>
                <%}%>
                <div class="ui inverted segment">
                    <div id='map' style='width:780px; height:500px;'></div>
                </div>

            </div>

        </div>


    </body>
</html>
