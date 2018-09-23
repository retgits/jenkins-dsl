.PHONY: test

#--- Help ---
help:
	@echo 
	@echo Makefile targets
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' Makefile | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	@echo 

#--- Test targets ---
.PHONY: test
test: ## Check if all GitHub repos have a mirror in the projects folder
	@chmod +x ./scripts/validate.sh
	@./scripts/validate.sh
