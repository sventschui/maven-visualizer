<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<style>
		.node {
			
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
			
				var graph = ${json.data}
				
			svg.append("svg:defs").selectAll("marker")
				    .data(["marker"])
				  .enter().append("svg:marker")
				    .attr("id", "myMarker")
				    .attr("viewBox", "0 -5 10 10")
				    .attr("refX", 21)
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
			      .data(graph.nodes)
			    .enter().append("svg:text")
			      .attr("class", "node")
			      .style("fill", function(d) { return "black"; })
			      .text(function(d) { return d.artifactId; })
			      .call(force.drag);
			
			  force.on("tick", function() {
			    link.attr("x1", function(d) { return d.source.x; })
			        .attr("y1", function(d) { return d.source.y; })
			        .attr("x2", function(d) { return d.target.x; })
			        .attr("y2", function(d) { return d.target.y; });
			
			    node.attr("x", function(d) { return d.x; })
			        .attr("y", function(d) { return d.y; });
			  });
			
		</script>
	</body>
</html>