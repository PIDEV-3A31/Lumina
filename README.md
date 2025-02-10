# Lumina

## Installation et Configuration locale chez les contributeurs

### 1. Création du dossier local
Créez un nouveau dossier portant le nom du projet en local.

### 2. Ouvrir Git Bash
Faites un clic droit et ouvrez Git Bash (si cela ne fonctionne pas, ouvrez cmd puis tapez `git --version` ou simplement `git`).

### 3. Clonage du dépôt
Dans Git Bash, tapez la commande suivante pour cloner le dépôt :
```bash
git clone https://github.com/PIDEV-3A31/Lumina.git
```
### 4. Ouvrir l'IDE
Ouvrez votre IDE (par exemple, VSCode) en utilisant la commande suivante :
```bash
code .
```
### 5. Création d'une nouvelle branche
Avant de modifier le code, créez une nouvelle branche pour votre travail :
```bash
git checkout -b nom-de-votre-branche
```
### 6. Travaillez sur votre branche
Effectuez vos modifications, ajouts ou corrections.

### 7. Ajout et Commit
Après avoir terminé vos modifications, ajoutez-les au suivi Git et effectuez un commit :
```bash
git add .
git commit -m "Texte du commit"
```
Le texte du commit devrait refleter le changement effectué sur le travail que vous avez fait sur votre branche.

### 8. Push vers la branche distante
Poussez vos modifications sur la branche distante que vous avez créée :
```bash
git push origin nom-de-votre-branche
```
### 9. Vérification sur GitHub
Vérifiez sur GitHub que vos modifications ont bien été uploadées.
