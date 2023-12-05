# PostgreSQL
kubectl apply \
	-f postgresql-claim0-persistentvolume.yaml \
	-f postgresql-claim0-persistentvolumeclaim.yaml \
	-f postgresql-initial-data-configmap.yaml \
	-f postgresql-deployment.yaml \
	-f postgresql-service.yaml
