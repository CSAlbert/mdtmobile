/**
 * 自建函数修改内容
 */

//发送邮件弹窗
function confirmDialog() {
    mui.confirm("", "是否通过邮件将报表数据发送给您？", ["取消", "发送"], function (e) {
        if (e.index == 1) {
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

// 存appUserName和logonToken到session中
function savaDataToSession() {
    $.ajax({
        url: "CIEDRanking/InitializeBo",
        type: "POST",    // 提交方式
        // async: false,
        data: appUser,
        success: function (data) {
            console.log("成功设置session");
            ajax_Region();
            QTDButton();
        },
        error: function (data) {
            console.log("设置session失败ff失败失败")
        }
    })
}


//TotalRanking函数
function totalRanking() {
    TotalOrBrady = 0;//0表示Total，1表示Brady
    $("#totalRankingName").css("color", "#00A8E1");
    $("#bradyRankingName").css("color", "#A7A7A7");
    $("#bradyRankingIcon").attr("src", rankIcon[3]);
    if (TotalclickNumber % 2 == 0) {
        asc_dsc_1 = 0;
        $("#totalRankingIcon").attr("src", rankIcon[2]);
    } else {
        asc_dsc_1 = 1;
        $("#totalRankingIcon").attr("src", rankIcon[0]);
    }

    sort();
    TotalclickNumber++;
}


function bradyRanking() {
    TotalOrBrady = 1;
    $("#totalRankingName").css("color", "#A7A7A7");
    $("#bradyRankingName").css("color", "#00A8E1");
    $("#totalRankingIcon").attr("src", rankIcon[3]);

    if (BradyclickNumber % 2 == 0) {
        asc_dsc_1 = 0;
        $("#bradyRankingIcon").attr("src", rankIcon[2]);
    } else {
        asc_dsc_1 = 1;
        $("#bradyRankingIcon").attr("src", rankIcon[0]);
    }
    sort();
    BradyclickNumber++;
}

// var appUser = {"name":"潘丽芬"}

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
            totalRanking();
        },
        error: function (data, XMLHttpRequest, textStatus, errorThrown) {
            alert("Region数据获取失败！");
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
        // async: false,
        beforeSend: function () {
            showLoading(true);
        },
        complete: function (data) {
            showLoading(false);

        },
        success: function (data) {
            t2 = data;
            sort();
        },
        error: function (data, XMLHttpRequest, textStatus, errorThrown) {
            alert("District数据获取失败！");
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
        // async: false,
        beforeSend: function () {
            showLoading(true);
        },
        complete: function () {
            showLoading(false);

        },
        success: function (data) {
            t3 = data;
            sort();
        },
        error: function (data, XMLHttpRequest, textStatus, errorThrown) {
            alert("TSR数据获取失败！");
        }
    })
}

//具体数据展示


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


