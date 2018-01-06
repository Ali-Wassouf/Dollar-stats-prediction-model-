rdollar <- read.csv(file.choose(),header = T)
dollar <-rdollar[!duplicated(rdollar[,1:2]),]
contrasts(dollar$city) = contr.treatment(6)
dollar$date <- as.numeric(as.Date(dollar$date, "%Y-%m-%d"))

require(ggplot2)
require(latticeExtra)
library(zoo)
ggplot( data = dollar, aes( date, (sell+buy)/2 )) + geom_line()
xyplot(dollar$buy ~ dollar$date|dollar$city,xlab = "date",ylab = "buy")
modelBuy <- lm (buy ~ city + date , data = dollar)
modelSell <- lm (sell ~ city +date ,data = dollar)
modelDate <- lm(date ~ city +buy+sell , data = dollar)
modeltest <- lm(Date ~ buy,data = dollar)
summary(modelBuy)
predicted_Date <-predict(modelDate , newdata = data.frame(buy = 10000, sell = 10000,city = "Damascus"))
as.Date(predicted_Date)
predicted_buy <- predict(modelSell,newdata = data.frame(
  city = "Damascus",date =  as.numeric(as.Date("2016-5-29","%Y-%m-%d")))) 
predicted_buy
