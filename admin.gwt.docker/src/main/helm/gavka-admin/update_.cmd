kubectl create namespace %1
helm lint
helm upgrade %1-gavka-admin . --namespace %1 -i