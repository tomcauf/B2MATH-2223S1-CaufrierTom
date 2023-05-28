# Projet de Mathématiques

## Introduction
Les travaux pratiques de mathématiques appliquées ont pour objectif de vous amener à mettre en œuvre des notions mathématiques dans le contexte d’un développement informatique.
Le projet de cette année porte sur les sujets suivants :
- Arbre lexicographique ;
- Résolution d’une grille de Boggle ;
- Cryptanalyse d’un chiffrement par substitution.

## Technologies
- Java 18
- JGraphT 1.5.1

## Installation
1. Cloner le projet
2. Importer le projet dans votre IDE
3. Ajouter la librairie JGraphT 1.5.1 dans votre projet
4. Lancer le projet

## Arbre lexicographique
### Présentation
Un arbre lexicographique est une structure de données arborescente qui permet de mémoriser un ensemble de mots, sans répéter les parties communes des différents mots. Elle est par exemple utilisée pour stocker les mots d’un dictionnaire.

### Implémentation
L’implémentation de l’arbre lexicographique est réalisée dans la classe `LexicographicTree`. Cette classe contient un attribut `root` de type `Node` qui représente la racine de l’arbre. La classe `Node` contient un attribut `children` de type Node qui représente les fils du noeud courant. Elle contient également un attribut `siblings` de type `Node` qui représente la soeur du noeud courant. Enfin, elle contient un attribut `letter` de type `char` qui représente la lettre du noeud courant. La classe `Node` contient également un attribut `isEndOfWord` de type `boolean` qui permet de savoir si le noeud courant est une fin de mot.

## Résolution d’une grille de Boggle
### Présentation
`Boggle est un jeu de lettres conçu par Alan Turoff et déposé par Parker Brothers / Hasbro, Inc.`<br>
Le jeu commence par le mélange d'un plateau (carré) de 16 dés à 6 faces, généralement en le secouant. Chaque dé possède une lettre différente sur chacune de ses faces. Les dés sont rangés sur le plateau 4 par 4, et seule leur face supérieure est visible. Après cette opération, un compte à rebours de 3 minutes est lancé et tous les joueurs commencent à jouer.
Chaque joueur cherche des mots pouvant être formés à partir de lettres adjacentes du plateau. Par « adjacentes », il est sous-entendu horizontalement, verticalement, ou en diagonale. Les mots doivent être de 3 lettres au minimum, peuvent être au singulier ou au pluriel, conjugués ou non, mais ne doivent pas utiliser plusieurs fois le même dé pour le même mot. »
[Source : https://fr.wikipedia.org/wiki/Boggle](https://fr.wikipedia.org/wiki/Boggle)

### Implémentation
L’implémentation de la résolution d’une grille de Boggle est réalisée dans la classe `Boggle` avec la biliothèque JGraphT. Cette classe contient un attribut `graph` de type `SimpleGraph<Dice, DefaultEdge>` qui représente la grille de Boggle. Elle contient également un attribut `lexicographicTree` de type `LexicographicTree` qui représente l’arbre lexicographique (dictionnaire). Le Graph contient des noeuds de type `Dice` qui représente un dé. La classe `Dice` contient un attribut `letter` de type `char` qui représente la lettre du dé. Elle contient également un attribut `isVisited` de type `boolean` qui permet de savoir si le dé a été visité ou non.

## Cryptanalyse d’un chiffrement par substitution
### Présentation
Le principe du chiffrement par substitution simple a été expliqué au cours théorique.
Étant donné un cryptogramme généré au moyen d’une substitution simple, est-il possible de déterminer de manière automatique la substitution inverse et donc de retrouver le texte en clair sans devoir recourir à une attaque par force brute ?
L’analyse fréquentielle fournit une première approche, mais l’alphabet de déchiffrement estimé sur la seule base de l’apparition des lettres dans le cryptogramme est rarement suffisant.
L’objectif de cet exercice est d’utiliser un dictionnaire pour guider le processus de cryptanalyse.

### Implémentation
L’implémentation de la cryptanalyse d’un chiffrement par substitution est réalisée dans la classe `DictionaryBasedAnalysis`. Cette classe contient un attribut `words` de type `String[]` qui représente les mots valident du cryptogramme (mots >= 3 caractères). Elle contient également un attribut `dict` de type `LexicographicTree` qui représente l’arbre lexicographique (dictionnaire).