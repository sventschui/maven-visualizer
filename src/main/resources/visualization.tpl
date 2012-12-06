<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<style>
		.node {
			font-family: Arial, sans-serif;
			font-size: 11px;
			line-height: 16px;
		}
		
		.artifactId {
			font-weight: bold;
		}
		
		.link {
			stroke: #999;
			stroke-opacity: .6;
		}
		</style>
	</head>
	<body>
		<script src="http://d3js.org/d3.v3.min.js"></script>
		<script>
			
			var width = document.documentElement.clientWidth,
			    height = document.documentElement.clientHeight;
			
			var force = d3.layout.force()
			    .charge(-350)
			    .linkDistance(300)
			    .size([width, height]);
			
			var svg = d3.select("body").append("svg")
			    .attr("width", width)
			    .attr("height", height);
			
				var graph = #jsondata#
				
			svg.append("svg:defs").selectAll("marker")
				    .data(["marker"])
				  .enter().append("svg:marker")
				    .attr("id", "myMarker")
				    .attr("viewBox", "0 -5 10 10")
				    .attr("refX", 0)
				    .attr("refY", 0)
				    .attr("markerWidth", 6)
				    .attr("markerHeight", 6)
				    .attr("orient", "auto")
				  .append("svg:path")
				    .attr("d", "M0,-5L10,0L0,5");
			
			  force
			      .nodes(graph.nodes)
			      .links(graph.links)
			      .start();
			
			  var link = svg.selectAll("line.link")
			      .data(graph.links)
			    .enter().append("line")
			      .attr("class", "link")
			      .style("stroke-width", 1)
				    .attr("marker-end", function(d) { return "url(#myMarker)"; });
			
			  var node = svg.selectAll(".node")
			      .data(graph.nodes).enter()
			      .append("svg:svg")
			      .attr("class", "node");
			      
			  node.append("svg:text")
			      .style("fill", function(d) { return "black"; })
			      .text(function(d) { return d.artifactId; })
			      .attr("class", "artifactId")
			  	  .attr("x", 0)
			  	  .attr("y", 10);
			      
			  node.append("svg:text")
			      .style("fill", function(d) { return "#555555"; })
			      .text(function(d) { return d.groupId; })
			      .attr("class", "groupId")
			  	  .attr("x", 0)
			  	  .attr("y", 25);
			      
			  node.append("svg:text")
			      .style("fill", function(d) { return "#555555"; })
			      .text(function(d) { return d.version; })
			      .attr("class", "version")
			  	  .attr("x", 0)
			  	  .attr("y", 40);
			  	  
			  node.call(force.drag);
			
			  force.on("tick", function() {
			    link.attr("x1", function(d) { return d.source.x - 10; })
			        .attr("y1", function(d) { return d.source.y + 20; })
			        .attr("x2", function(d) { return d.target.x - 10; })
			        .attr("y2", function(d) { return d.target.y + 20; });
			
			    node.attr("x", function(d) { return d.x; })
			        .attr("y", function(d) { return d.y; });
			  });
			
		</script>
	</body>
</html>