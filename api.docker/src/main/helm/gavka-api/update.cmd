kubectl create namespace %1
helm lint
helm upgrade %1-gavka-api . --debug --namespace %1 -i