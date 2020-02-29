kubectl create namespace %1
helm lint
helm upgrade %1-gavka-ui . --namespace %1 -i