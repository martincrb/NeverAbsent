ok <- seq(1, 1, 100)
not_ok <- seq(0,0,100)
dataset_ok[,"label"] <- ok
dataset_wrong[,"label"] <- not_ok

dataset <- rbind(dataset_ok, dataset_wrong)

trainIdx <- sample(1:200, 150)


km <- kmeans(dataset[, -4], 2, iter.max = 10)

plot(dataset[, -4], col = km$cluster)

pairs(~dataset_ok$fftResults+dataset_ok$apEnResults+dataset_ok$sampEnResults,data=dataset, 
      main="Simple Scatterplot Matrix")


pairs(~dataset_wrong$fftResults+dataset_wrong$apEnResults+dataset_wrong$sampEnResults,data=dataset, 
      main="Simple Scatterplot Matrix")
#train <- dataset[,trainIdx]
#test <- dataset[,-trainIdx]

#model <- lda(label~., dataset)

dataset$label <- as.factor(dataset$label)

lda <- train(dataset$label~., data=dataset, method = "lda", trControl = trainControl(method = "cv", number=10))
lda
gl <- train(dataset$label~., data=dataset, method = "glm", trControl = trainControl(method = "cv", number=10))
gl

m <- train(dataset$label~., data=dataset, method = "svmLinear2", trControl = trainControl(method = "cv", number=10))

m2 <- train(dataset$label~dataset$fftResults+dataset$sampEnResults, data=dataset, method = "svmLinear2", trControl = trainControl(method = "cv", number=10))
m2


m

plot(dataset_wrong$apEnResults,dataset_wrong$sampEnResults)

plot(dataset_ok$apEnResults,dataset_ok$sampEnResults)



onlyfft <- svm(dataset$label~dataset$fftResults, data = dataset,kernel="linear", gamma=2, cost=0.25)

all <- svm(dataset$label~., data = dataset,kernel="linear", gamma=2, cost=0.25)

entropy <- svm(dataset$label~dataset$apEnResults+dataset$sampEnResults, data = dataset,kernel="linear", gamma=2, cost=0.25)

w = t(all$coefs) %*% all$SV
w
entropy$coef0

pred <- predict(entropy,dataset[,0:3])
t_true <- dataset[,4]

table(pred,t_true)

# compute testing error (in %)
(sum(pred != t_true)/length(t_true))




pred <- predict(m,dataset[,0:3])
t_true <- dataset[,4]

table(pred,t_true)

# compute testing error (in %)
(sum(pred != t_true)/length(t_true))
