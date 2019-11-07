/**
 * 自建函数
 */

//body里面的onload中需要打开时默认调用的函数
window.onload = function () {
    QTDButton();
    // ajax_TSR();
    // ajax_District();
    ajax_Region();
}

//发送邮件弹窗
function confirmDialog() {
    mui.confirm("", "是否通过邮件将报表数据发送给您？", ["取消", "发送"], function(e) {
        if(e.index == 1) {
            alert("已发送邮件");
        } else {
            alert("取消发送邮件");
        }

    });
}

//点击QTD底部变白条
function QTDButton() {
    var a = document.getElementById("QTD1");
    var b = document.getElementById("QTD2");
    a.style.backgroundColor = "white";
    b.style.backgroundColor = "#02A8E2";

}

//点击YTD底部变白条
function YTDButton() {
    var a = document.getElementById("QTD1");
    var b = document.getElementById("QTD2");
    a.style.backgroundColor = "#02A8E2";
    b.style.backgroundColor = "white";
}

//删除说明框
function deleteDescription() {
    section.style.display = "none";
    blank.style.display = "none";
    X.style.display = "none";
}

//等待页面效果
function showLoading(show) {

    if (show) {
        document.getElementById("circulation").style.display = "none";
        document.getElementById("loading").style.display = "block";
    } else {
        document.getElementById("circulation").style.display = "block";
        document.getElementById("loading").style.display = "none";
    }
}


var QYsign = 0;
var t1 = [];
var t2 = [];
var t3 = [];
var tabType = 0;
var asc_dsc_1 = 0;
var TotalOrBrady = 0;
var description = null;
var date = null;

//QTD、YTD转换
function changeQYsign(sign) {
    if (sign == QYsign) {
        return;
    } else {
        if (sign == 0) {
            QYsign = 0;
            sort();
        } else {
            QYsign = 1;
            sort();
        }
    }
}

//排序方法
function QTD_Brady_Ranking_Sort(x, y) {
    if (asc_dsc_1 == 0) {
        return Number(x["QTD Brady Ranking"]) - Number(y["QTD Brady Ranking"]);
    } else {
        return Number(y["QTD Brady Ranking"]) - Number(x["QTD Brady Ranking"]);
    }
}

function YTD_Brady_Ranking_Sort(x, y) {
    if (asc_dsc_1 == 0) {
        return Number(x["YTD Brady Ranking"]) - Number(y["YTD Brady Ranking"]);
    } else {
        return Number(y["YTD Brady Ranking"]) - Number(x["YTD Brady Ranking"]);
    }
}

function QTD_Total_Ranking_Sort(x, y) {
    if (asc_dsc_1 == 0) {
        return Number(x["QTD Total Ranking"]) - Number(y["QTD Total Ranking"]);
    } else {
        return Number(y["QTD Total Ranking"]) - Number(x["QTD Total Ranking"]);
    }
}

function YTD_Total_Ranking_Sort(x, y) {
    if (asc_dsc_1 == 0) {
        return Number(x["YTD Total Ranking"]) - Number(y["YTD Total Ranking"]);
    } else {
        return Number(y["YTD Total Ranking"]) - Number(x["YTD Total Ranking"]);
    }
}

//数据排序及展示
function sort() {
    if (tabType == 1) {
        if (QYsign == 0) {

            if (TotalOrBrady == 0) {
                t1.mainData.sort(QTD_Total_Ranking_Sort);
            } else {
                t1.mainData.sort(QTD_Brady_Ranking_Sort);
            }
            view_QTD_Region(t1);
        } else {
            if (TotalOrBrady == 0) {
                t1.mainData.sort(YTD_Total_Ranking_Sort);
            } else {
                t1.mainData.sort(YTD_Brady_Ranking_Sort);
            }
            view_YTD_Region(t1);
        }
    } else if (tabType == 2) {
        if (QYsign == 0) {
            if (TotalOrBrady == 0) {
                t2.mainData.sort(QTD_Total_Ranking_Sort);
            } else {
                t2.mainData.sort(QTD_Brady_Ranking_Sort);
            }
            view_QTD_District(t2);
        } else {
            if (TotalOrBrady == 0) {
                t2.mainData.sort(YTD_Total_Ranking_Sort);
            } else {
                t2.mainData.sort(YTD_Brady_Ranking_Sort);
            }
            view_YTD_District(t2);
        }
    } else if (tabType == 3) {
        if (TotalOrBrady == 0) {
            t3.mainData.sort(QTD_Total_Ranking_Sort);
        } else {
            t3.mainData.sort(QTD_Brady_Ranking_Sort);
        }
        view_QTD_TSR(t3);
    }
}


//Region数据
function ajax_Region() {
    region_block.style.backgroundColor = "#D6F4FE";
    district_block.style.backgroundColor = "white";
    TSR_block.style.backgroundColor = "white";
    tabType = 1;

    if (t1.length != 0) {
        sort();
        return;
    }
    $.ajax({
        url: "CIEDRanking/Region",
        type: "POST",    // 提交方式
        dataType: "json",
        asycn: false,
        beforeSend: function () {
            showLoading(true);
        },
        complete: function (data) {
            showLoading(false);
        },
        success: function (data) {
            t1 = data;

            /*日期数据*/
            var dateString = data.formattedDate;
            date = dateString.substring(dateString.length - 20);

            /*将日期数据放入页面元素中*/
            $("#date").append(date);
            // document.getElementById("date").innerHTML = date;

            /*取得描述信息，并放入相应位置*/
            description = data.description.replace(/\r\n/g, "<br/>");
            $("#description").append(description);
            // document.getElementById("description").innerHTML = description;


            sort();
        },
        error: function (data, XMLHttpRequest, textStatus, errorThrown) {
            alert("Region获取数据失败！");
        }
    })
}

//District数据
function ajax_District() {
    region_block.style.backgroundColor = "white";
    district_block.style.backgroundColor = "#D6F4FE";
    TSR_block.style.backgroundColor = "white";
    tabType = 2;
    if (t2.length != 0) {
        sort();
        return;
    }
    $.ajax({
        url: "CIEDRanking/District",
        type: "POST",    // 提交方式
        dataType: "json",
        asycn: false,
        beforeSend: function () {
            showLoading(true);
        },
        complete: function (data) {
            showLoading(false);

        },
        success: function (data) {
            t2 = data;

            /*日期数据*/
            var dateString = data.formattedDate;
            date = dateString.substring(dateString.length - 20);

            /*将日期数据放入页面元素中*/
            $("#date").append(date);
            // document.getElementById("date").innerHTML = date;

            /*取得描述信息，并放入相应位置*/
            description = data.description.replace(/\r\n/g, "<br/>");
            $("#description").append(description);
            // document.getElementById("description").innerHTML = description;

            sort();
        },
        error: function (data, XMLHttpRequest, textStatus, errorThrown) {
            alert("District获取数据失败！");
        }
    })
}

//TSR数据
function ajax_TSR() {
    region_block.style.backgroundColor = "white";
    district_block.style.backgroundColor = "white";
    TSR_block.style.backgroundColor = "#D6F4FE";
    tabType = 3;
    if (t3.length != 0) {
        sort();
        return;
    }

    $.ajax({
        url: "CIEDRanking/TSR",
        type: "POST",    // 提交方式
        dataType: "json",
        asycn: false,
        beforeSend: function () {
            showLoading(true);
        },
        complete: function () {
            showLoading(false);

        },
        success: function (data) {
            t3 = data;
            /*日期数据*/
            var dateString = data.formattedDate;
            date = dateString.substring(dateString.length - 20);

            /*将日期数据放入页面元素中*/
            $("#date").append(date);
            // document.getElementById("date").innerHTML = date;

            /*取得描述信息，并放入相应位置*/
            description = data.description.replace(/\r\n/g, "<br/>");
            $("#description").append(description);
            // document.getElementById("description").innerHTML = description;

            sort();
        },
        error: function (data, XMLHttpRequest, textStatus, errorThrown) {
            alert("TSR获取数据失败！");
        }
    })
}

//具体数据展示
var index_Name = ["Total Ranking", "Total Ach%", "Total Gth% vs LY", "Brady Ranking", "Brady Ach%", "Brady Gth% vs LY"];
var QTD_Index = ["QTD Total Ranking", "QTD Total Ach%", "QTD Total Gth% vs LY", "QTD Brady Ranking", "QTD Brady Ach%", "QTD Brady Gth% vs LY"];
var YTD_Index = ["YTD Total Ranking", "YTD Total Ach%", "YTD Total Gth% vs LY", "YTD Brady Ranking", "YTD Brady" +
" Ach%", "YTD Brady Gth% vs LY"];
var QTD_Index_TSR = ["QTD Total Ranking", "QTD Total Ach%", "QTD Gth% vs LY", "QTD Brady Ranking", "QTD Brady" +
" Ach%", "QTD Brady Gth% vs LY"];


var ranking_images = ['images/pm1.png', 'images/pm2.png', 'images/pm3.png'];

//根据数据生成详细内容列表（QTD-Region）
function view_QTD_Region(data) {
    var size = data.mainData.length;
    var div = "";
    var imgNumber = 0;

    for (var i = 0; i < size; i++) {
        var li = "";
        var ul = "";
        var header1 = "";
        if (asc_dsc_1 == 1) {
            imgNumber = size - 1 - i;
        } else if (asc_dsc_1 == 0) {
            imgNumber = i;
        }

        for (var j = 0; j < 6; j++) {
            li += "<li class='mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3' style='width:" +
                " 33.333%;border:hidden'><span " +
                "class='style-content-data' >" + data.mainData[i][QTD_Index[j]] + "</span><div" +
                " class='mui-media-body'style='color:darkgray;font-size:12px;'>" + index_Name[j] + "</div></li>";
        }
        if (imgNumber < 3) {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<img src='" + ranking_images[imgNumber] + "'style='height: 25px;width: 25px;vertical-align:middle; '>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 8px '>" + data.mainData[i]["Region"] +
                "</span></a></div>";
        } else {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<span style='color: #0e0e0e;margin-left: 5px'>" + (imgNumber + 1) + "</span>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 18px'>" + data.mainData[i]["Region"] +
                "</span></a></div>";
        }


        ul = "<p style='height:2px;'></p><div class='mui-card'>" + header1 + "<div class='mui-card-content'>" +
            "<ul class='mui-table-view mui-grid-view mui-grid-9'>" + li + "</ul></div></div>";

        div = div + ul;
    }
    console.time("re");
    document.getElementById('circulation').innerHTML = div;
    // $("#circulation").append(div);
    console.timeEnd("re");
}

//根据数据生成详细内容列表（YTD-Region）
function view_YTD_Region(data) {
    var size = data.mainData.length;
    var div = "";
    var imgNumber = 0;
    for (var i = 0; i < size; i++) {
        var li = "";
        var ul = "";
        var header1 = "";
        if (asc_dsc_1 == 1) {
            imgNumber = size - 1 - i;
        } else if (asc_dsc_1 == 0) {
            imgNumber = i;
        }

        for (var j = 0; j < 6; j++) {
            li += "<li class='mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3' style='width:" +
                " 33.333%;border:hidden'><span " +
                "class='style-content-data' >" + data.mainData[i][YTD_Index[j]] + "</span><div" +
                " class='mui-media-body'style='color:darkgray;font-size:12px;'>" + index_Name[j] + "</div></li>";
        }
        if (imgNumber < 3) {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<img src='" + ranking_images[imgNumber] + "'style='height: 25px;width: 25px;vertical-align:middle; '>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 8px '>" + data.mainData[i]["Region"] +
                "</span></a></div>";
        } else {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<span style='color: #0e0e0e;margin-left: 5px'>" + (imgNumber + 1) + "</span>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 18px'>" + data.mainData[i]["Region"] +
                "</span></a></div>";
        }


        ul = "<p style='height:2px;'></p><div class='mui-card'>" + header1 + "<div class='mui-card-content'>" +
            "<ul class='mui-table-view mui-grid-view mui-grid-9'>" + li + "</ul></div></div>";

        div = div + ul;
    }
    document.getElementById('circulation').innerHTML = div;

}

//根据数据生成详细内容列表（QTD——District）
function view_QTD_District(data) {

    var size = data.mainData.length;
    var div = "";
    var imgNumber = 0;
    for (var i = 0; i < size; i++) {
        var li = "";
        var ul = "";
        var header1 = "";
        if (asc_dsc_1 == 1) {
            imgNumber = size - 1 - i;
        } else if (asc_dsc_1 == 0) {
            imgNumber = i;
        }
        for (var j = 0; j < 6; j++) {
            li += "<li class='mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3' style='width:" +
                " 33.333%;border:hidden'><span " +
                "class='style-content-data' >" + data.mainData[i][QTD_Index[j]] + "</span><div" +
                " class='mui-media-body'style='color:darkgray;font-size:12px;'>" + index_Name[j] + "</div></li>";
        }


        if (imgNumber < 3) {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<img src='" + ranking_images[imgNumber] + "'style='height: 25px;width: 25px;vertical-align:middle; '>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 8px '>" + data.mainData[i]["District"] +
                "</span></a></div>";
        } else {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<span style='color: #0e0e0e;margin-left: 5px'>" + (imgNumber + 1) + "</span>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 18px'>" + data.mainData[i]["District"] +
                "</span></a></div>";
        }

        ul = "<p style='height:2px;'></p><div class='mui-card'>" + header1 + "<div class='mui-card-content'>" +
            "<ul class='mui-table-view mui-grid-view mui-grid-9'>" + li + "</ul></div></div>";

        div = div + ul;
    }
    console.time("region");
    document.getElementById('circulation').innerHTML = div;
    console.timeEnd("region");
}

//根据数据生成详细内容列表（YTD-District）
function view_YTD_District(data) {

    var size = data.mainData.length;
    var div = "";
    var imgNumber = 0;
    for (var i = 0; i < size; i++) {
        var li = "";
        var ul = "";
        var header1 = "";
        if (asc_dsc_1 == 1) {
            imgNumber = size - 1 - i;
        } else if (asc_dsc_1 == 0) {
            imgNumber = i;
        }
        for (var j = 0; j < 6; j++) {
            li += "<li class='mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3' style='width:" +
                " 33.333%;border:hidden'><span " +
                "class='style-content-data' >" + data.mainData[i][YTD_Index[j]] + "</span><div" +
                " class='mui-media-body'style='color:darkgray;font-size:12px;'>" + index_Name[j] + "</div></li>";
        }

        if (imgNumber < 3) {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<img src='" + ranking_images[imgNumber] + "'style='height: 25px;width: 25px;vertical-align:middle; '>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 8px '>" + data.mainData[i]["District"] +
                "</span></a></div>";
        } else {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<span style='color: #0e0e0e;margin-left: 5px'>" + (imgNumber + 1) + "</span>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 18px'>" + data.mainData[i]["District"] +
                "</span></a></div>";
        }

        ul = "<p style='height:2px;'></p><div class='mui-card'>" + header1 + "<div class='mui-card-content'>" +
            "<ul class='mui-table-view mui-grid-view mui-grid-9'>" + li + "</ul></div></div>";

        div = div + ul;
    }
    document.getElementById('circulation').innerHTML = div;

}

//TSR-QTD（TSR只有QTD）
function view_QTD_TSR(data) {
    var size = data.mainData.length;
    var div = "";
    var imgNumber = 0;
    for (var i = 0; i < size; i++) {
        var li = "";
        var ul = "";
        var header1 = "";
        if (asc_dsc_1 == 1) {
            imgNumber = size - 1 - i;
        } else if (asc_dsc_1 == 0) {
            imgNumber = i;
        }

        for (var j = 0; j < 6; j++) {
            li += "<li class='mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-3' style='width:" +
                " 33.333%;border:hidden'><span " +
                "class='style-content-data' >" + data.mainData[i][QTD_Index_TSR[j]] + "</span><div" +
                " class='mui-media-body'style='color:darkgray;font-size:12px;'>" + index_Name[j] + "</div></li>";
        }

        if (imgNumber < 3) {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<img src='" + ranking_images[imgNumber] + "'style='height: 25px;width: 25px;vertical-align:middle; '>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 8px '>" + data.mainData[i]["TSR"] +
                "</span></a></div>";
        } else {
            header1 = "<div class='mui-card-header' style='height: 25px;'><a class='mui-control-item'>" +
                "<span style='color: #0e0e0e;margin-left: 5px'>" + (imgNumber + 1) + "</span>" +
                "&nbsp;<span style='font: 微软雅黑; color: #000000;font-size:15px;margin-left: 18px'>" + data.mainData[i]["TSR"] +
                "</span></a></div>";
        }


        ul = "<p style='height:2px;'></p><div class='mui-card'>" + header1 + "<div class='mui-card-content'>" +
            "<ul class='mui-table-view mui-grid-view mui-grid-9'>" + li + "</ul></div></div>";

        div = div + ul;
    }
    console.time("tsr");
    $("#circulation").html(div);
    console.timeEnd("tsr");
}


