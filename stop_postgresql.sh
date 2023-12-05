kubectl delete -f postgresql-deployment.yaml \
	-f postgresql-service.yaml \
	-f postgresql-initial-data-configmap.yaml \
	-f postgresql-claim0-persistentvolumeclaim.yaml \
	-f postgresql-claim0-persistentvolume.yaml
