let margin = { top: 40, right: 20, bottom: 40, left: 20 };

let baseSurveys = {};

function loadSurveys() {
  let surveys =
    '[{"id":"1","path":"surveys/aire_menores_last.xml","title":"Niños"},{"id":"2","path":"surveys/adolescentes_adultos_last.xml","title":"Jóvenes y adultos"},{"id":"3","path":"surveys/eval_menores_last.xml","title":"Evaluación para niños"},{"id":"4","path":"surveys/eval_adolescentes_adultos.xml","title":"Evaluación para jóvenes y adultos"}]';
  JSON.parse(surveys).forEach((item) =>
    processSurveyStructure(item["id"], item["path"])
  );
}

function processSurveyStructure(id, path) {
  let urlBase = "http://youilab.ipicyt.edu.mx/sensky_store/";
  $.get(urlBase + path, function (xml) {
    tmpSurvey = {};
    tmpSurvey["title"] = $(xml).find("survey>title")[0].textContent;
    tmpSurvey["questions"] = buildQuestionsStructure($(xml).find("question"));
    baseSurveys[id] = tmpSurvey;
  });
}

function buildQuestionsStructure(questions) {
  questionsDict = [];
  $.each(questions, function (k, v) {
    let tmpQuestion = {};
    tmpQuestion["id"] = $(v).attr("id");
    tmpQuestion["type"] = $(v).attr("type");
    tmpQuestion["text"] = $(v).find("text")[0].textContent;
    tmpQuestion["answerId"] = "Q" + (k + 1); //$(v).attr("questionId");
    if ($(v).attr("type") == "multioption") {
      tmpQuestion["data"] = [];
      let counter = 0;
      $.each($(v).find("option"), function (i, opt) {
        tmpQuestion["data"].push({
          group: opt.textContent,
          frecc: 0,
        });
        counter = i;
      });
      tmpQuestion["size"] = counter < 3 ? "small" : "big";
      tmpQuestion["addData"] = function (selected) {
        this["data"].forEach(function (category) {
          if (category["group"] == selected) category["frecc"] += 1;
        });
      };
    } else if ($(v).attr("type") == "multianswer") {
      tmpQuestion["data"] = [];
      let counter = 0;
      $.each($(v).find("option"), function (i, opt) {
        tmpQuestion["data"].push({
          group: opt.textContent,
          frecc: 0,
        });
        counter = i;
      });
      tmpQuestion["size"] = counter < 3 ? "small" : "big";
      tmpQuestion["addData"] = function (row) {
        row.split(":").forEach((answer) => {
          let found = this["data"].filter(
            (freccObj) => freccObj.group == answer
          )[0];
          if (found) found.frecc += 1;
        });
      };
    } else if ($(v).attr("type") == "long-text") {
      tmpQuestion["data"] = "";
      tmpQuestion["size"] = "small";
      tmpQuestion["addData"] = function (text) {
        this["data"] += " " + text;
      };
    } else if ($(v).attr("type") == "lickertgroup") {
      tmpQuestion["data"] = {
        groups: [],
        variables: ["1", "2", "3", "4", "5"],
        frecc: [],
      };

      $.each($(v).find("option"), function (i, opt) {
        let category = opt.textContent;
        if (!tmpQuestion["data"].groups.includes(category)) {
          tmpQuestion["data"].groups.push(category);
          tmpQuestion["data"].frecc.push({
            name: category + ":1",
            value: 0,
          });
          tmpQuestion["data"].frecc.push({
            name: category + ":2",
            value: 0,
          });
          tmpQuestion["data"].frecc.push({
            name: category + ":3",
            value: 0,
          });
          tmpQuestion["data"].frecc.push({
            name: category + ":4",
            value: 0,
          });
          tmpQuestion["data"].frecc.push({
            name: category + ":5",
            value: 0,
          });
        }
      });

      tmpQuestion["size"] = "big";
      tmpQuestion["addData"] = function (row) {
        row.split("|").forEach((lickert) => {
          let found = this["data"].frecc.filter(
            (freccObj) => freccObj.name == lickert
          )[0];
          if (found) found.value += 1;
        });
      };
    } else {
      tmpQuestion["size"] = "big";
      tmpQuestion["addData"] = function (row) {};
    }

    questionsDict.push(tmpQuestion);
  });
  return questionsDict;
}

function filterCharts(node) {
  $("#statistics>.body").empty();
  precomposeCharts(node.value);
}

function precomposeCharts(surveyId) {
  //Copy from the main structure;
  let currentQuestions = baseSurveys[surveyId]["questions"].slice();
  frame.forEach(function (row) {
    //console.log("current survey: " + surveyId);
    if (row["surveyKey"] == surveyId) {
      currentQuestions.forEach(function (question) {
        console.log("currentRow: ", row);
        console.log("currentQuestion:", question)
        question.addData(row[question["answerId"]]);
      });
    }
  });
  buildCharts(currentQuestions);
}

function buildCharts(charts) {
  $.each(charts, function (key, value) {
    let id = value["answerId"];

    //Create the chart container

    $("#statistics>.body").append(
      '<div id="' +
        id +
        '" class="chart ' +
        value["size"] +
        " " +
        value["type"] +
        '"></div>'
    );

    if (value["type"] == "multioption" || value["type"] == "multianswer") {
      buildHistogram(id, value["text"], value["data"]);
    } else if (value["type"] == "long-text") {
      buildWordsCloud(id, value["text"], value["data"]);
    } else if (value["type"] == "lickertgroup") {
      buildLickertChart(id, value["text"], value["data"]);
    }

    //en alguna parte
  });
}

function buildLickertChart(id, title, data) {
  let width = $("#" + id).width() - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;
  let maxFrecc = 0;
  console.log(data);
  data.frecc.forEach((d) => {
    if (d.value > maxFrecc) maxFrecc = d.value;
  });

  let svg = d3
    .select("#" + id)
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  // Build X scales and axis:
  var x = d3.scaleBand().range([0, width]).domain(data.groups).padding(0.01);
  svg
    .append("g")
    .attr("transform", "translate(0," + height + ")")
    .call(d3.axisBottom(x))
    .selectAll(".tick text")
    .call(wrap, x.bandwidth());

  var y = d3
    .scaleBand()
    .range([height, 0])
    .domain(data.variables)
    .padding(0.01);
  svg.append("g").call(d3.axisLeft(y));

  var myColor = d3
    .scaleLinear()
    .range(["white", "#00c9c3"])
    .domain([0, maxFrecc]);

  svg
    .selectAll()
    .data(data.frecc, function (d) {
      return d.name;
    })
    .enter()
    .append("rect")
    .attr("x", function (d) {
      return x(d.name.split(":")[0]);
    })
    .attr("y", function (d) {
      return y(d.name.split(":")[1]);
    })
    .attr("width", x.bandwidth())
    .attr("height", y.bandwidth())
    .style("fill", function (d) {
      return myColor(d.value);
    });

  svg
    .append("text")
    .attr("x", width / 2)
    .attr("y", 0 - margin.top / 2)
    .attr("text-anchor", "middle")
    .attr("class", "chart-title")
    .style("font-size", "14px")
    .style("text-decoration", "underline")
    .text(title);
}

function buildWordsCloud(id, title, text) {
  var common = "poop,i,me,my";

  //console.log("building cloud: ", text);
  let word_count = {};
  let words = text.split(/[ '\-\(\)\*":;\[\]|{},.!?]+/);
  if (words.length == 1) {
    word_count[words[0]] = 1;
  } else {
    words.forEach(function (word) {
      word = word.toLowerCase();
      if (word != "" && common.indexOf(word) == -1 && word.length > 1) {
        if (word_count[word]) {
          word_count[word]++;
        } else {
          word_count[word] = 1;
        }
      }
    });
  }

  let svg_location = "#" + id;
  let width = $("#" + id).innerWidth() - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;

  let fill = d3.scaleOrdinal(d3.schemeCategory20);
  let word_entries = d3.entries(word_count);

  var xScale = d3
    .scaleLinear()
    .domain([
      0,
      d3.max(word_entries, function (d) {
        return d.value;
      }),
    ])
    .range([0, 100]);

  d3.layout
    .cloud()
    .size([width, height])
    .timeInterval(20)
    .words(word_entries)
    .fontSize(function (d) {
      return xScale(+d.value);
    })
    .text(function (d) {
      return d.key;
    })
    .rotate(function () {
      return ~~(Math.random() * 2) * 90;
    })
    .font("Impact")
    .on("end", draw)
    .start();

  function draw(words) {
    let svg = d3
      .select(svg_location)
      .append("svg")
      .attr("width", width)
      .attr("height", height)
      .append("g")
      .attr("transform", "translate(" + [width >> 1, height >> 1] + ")")
      .selectAll("text")
      .data(words)
      .enter()
      .append("text")
      .style("font-size", function (d) {
        console.log(xScale(d.value) + "px");
        return xScale(d.value) / 2 + "px";
      })
      .style("font-family", "Impact")
      .style("fill", function (d, i) {
        return fill(i);
      })
      .attr("text-anchor", "middle")
      .attr("transform", function (d) {
        console.log("d object: ", d);
        return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
      })
      .text(function (d) {
        return d.key;
      });
  }

  d3.layout.cloud().stop();
  d3.select(svg_location)
    .append("text")
    .call(wrap, width / 4)
    .attr("x", width / 2)
    .attr("y", 0 - margin.top / 2)
    .attr("text-anchor", "middle")
    .attr("class", "chart-title")
    .style("font-size", "14px")
    .style("text-decoration", "underline")
    .text(title);
}

function buildHistogram(id, title, data) {
  let width = $("#" + id).width() - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;

  let svg = d3
    .select("#" + id)
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  let x = d3.scaleBand().range([0, width]).padding(0.1);

  let y = d3.scaleLinear().range([height, 0]);

  // Scale the range of the data in the domains
  x.domain(Array.from(data, (current) => current.group));
  y.domain([
    0,
    d3.max(data, function (d) {
      return d.frecc;
    }),
  ]);

  // append the rectangles for the bar chart
  svg
    .selectAll(".bar")
    .data(data)
    .enter()
    .append("rect")
    .attr("class", "bar")
    .attr("x", function (d) {
      return x(d.group);
    })
    .attr("width", x.bandwidth())
    .attr("y", function (d) {
      return y(d.frecc);
    })
    .attr("height", function (d) {
      return height - y(d.frecc);
    });

  // add the x Axis
  svg
    .append("g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + height + ")")
    .call(d3.axisBottom(x))
    .selectAll(".tick text")
    .call(wrap, x.bandwidth());

  let maxFreccuency = d3.max(data, function (d) {
    return d.frecc;
  });

  // add the y Axis
  svg.append("g").call(
    d3
      .axisLeft(y)
      .ticks(maxFreccuency < 10 ? maxFreccuency : 10)
      .tickFormat(d3.format("d"))
  );

  svg
    .append("text")
    .call(wrap, width / 4)
    .attr("x", width / 2)
    .attr("y", 0 - margin.top / 2)
    .attr("text-anchor", "middle")
    .attr("class", "chart-title")
    .style("font-size", "14px")
    .style("text-decoration", "underline")
    .text(title);
}

function buildHorizontalBarsPlot(id, title, data) {
  let width = $("#" + id).width() - margin.left - margin.right,
    height = 300 - margin.top - margin.bottom;

  var svg = d3
    .select("#" + id)
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  var x = d3.scale
    .linear()
    .range([0, width])
    .domain([
      0,
      d3.max(data, function (d) {
        return d.frecc;
      }),
    ]);

  var y = d3.scale
    .ordinal()
    .rangeRoundBands([height, 0], 0.1)
    .domain(
      data.map(function (d) {
        return d.group;
      })
    );

  //make y axis to show bar names
  var yAxis = d3.svg
    .axis()
    .scale(y)
    //no tick marks
    .tickSize(0)
    .orient("left");

  var gy = svg.append("g").attr("class", "y axis").call(yAxis);

  var bars = svg.selectAll(".bar").data(data).enter().append("g");

  //append rects
  bars
    .append("rect")
    .attr("class", "bar")
    .attr("y", function (d) {
      return y(d.group);
    })
    .attr("height", y.rangeBand())
    .attr("x", 0)
    .attr("width", function (d) {
      return x(d.frecc);
    });

  //add a value label to the right of each bar
  bars
    .append("text")
    .attr("class", "label")
    //y position of the label is halfway down the bar
    .attr("y", function (d) {
      return y(d.group) + y.rangeBand() / 2 + 4;
    })
    //x position is 3 pixels to the right of the bar
    .attr("x", function (d) {
      return x(d.frecc) + 3;
    })
    .text(function (d) {
      return d.frecc;
    });
}

function wrap(text, width) {
  text.each(function () {
    var text = d3.select(this),
      words = text.text().split(/\s+/).reverse(),
      word,
      line = [],
      lineNumber = 0,
      lineHeight = 1.1, // ems
      y = text.attr("y"),
      dy = parseFloat(text.attr("dy")),
      tspan = text
        .text(null)
        .append("tspan")
        .attr("x", 0)
        .attr("y", y)
        .attr("dy", dy + "em");
    while ((word = words.pop())) {
      line.push(word);
      tspan.text(line.join(" "));
      if (tspan.node().getComputedTextLength() > width) {
        line.pop();
        tspan.text(line.join(" "));
        line = [word];
        tspan = text
          .append("tspan")
          .attr("x", 0)
          .attr("y", y)
          .attr("dy", ++lineNumber * lineHeight + dy + "em")
          .text(word);
      }
    }
  });
}
