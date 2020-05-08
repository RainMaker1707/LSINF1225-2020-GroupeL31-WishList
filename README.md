#WishList

##Base de donnée

####Table principales

Est une application basée sur un sytême de base de donnée relationnelles, celle-ci s'axant majoritairement autour d'une table (User) contenant la plupart des informations relatives aux utilisateurs.

Une table Wishlist permet de contenir les quelques informations relatives a une wishlist donnée (id, nom, taille & le mail de l'utilisateur propriétaire afin de le lié à cette wishlist).

Une table Wish contient quant à elle les informations relatives aux produits pouvant être contenu dans une WishList (id, nom, photo, prix, description, lieu d'achat).

####Tables relationnelles

Pour lié ces tables nous utilisons trois tables pour trois différentes relations:

<ul>
<li>Friend contient toutes les relations d'amitiés et leur stade (mail_host, relation, mail_requested) pour deux relation possible [0, 1] 0 signifie une demande en attende de l'utilisateur [mail_host] à l'utilisateur [mail_requested].</li>
  
<li>Perm contient les differentes permissions (mail, perm, wishlist_id) de tel façon que là permission est set à "wishlist [wihslist_id] caché pour l'utilisateur [mail]" quand elle contient 0 et inversement "l'utilisateur [mail] peut modifier la wishlist[wishlist_id] quand elle vaut 1.</li>
  
<li>Content contient toutes les relations où une wishlist contient un wish (wishlist_id, wish_id) une ligne présente signifie un produit contenu, si un produit doit etre supprimé d'une wishlist, la ligne représentant la relatione st simplement supprimée.</li>
</ul>

##Application

Pour se connecter et accéder au contenu de l'application ainsi que c'est fonctionnalité un utilisateur doit être au préalable enregistré dans la base de données pour se faire une page d'inscription découpée en deux étapes a été préparée, la première étape permet de remplir les champs obligatoire requis pour créer une ligne dans la table User, la deuxième étape permet de set les éléments voulu ou non en plus ou simplement de passer l'étapes d'ajout des éléments optionnels.

Afin de ne pas rendre l'application complétement vide des images par défault on été ajouté pour le profil utilisateur et les wishlist et wish ne possédant pas de photo.

Une fois connecter l'utilisateur se trouve sur la page "Main" de l'application, celle-ci regroupant ces wishlists et lui permettant d'accéder au menu de l'application apr le biais d'un click sur son image de profil en haut a droite.

####Le Menu

Le menu est accessible en tout temps par le click sur le bouton ou l'image de profil situé en haut à droite de l'application ( désactivé dans quelques cas comme la création de wish par exemple).

Le menu permettra à l'utilisateur connecté de voyager sur les activités suivantes:
<ul>
<li>L'acces a son profil</li>
<li>Voir ses WishLists</li>
<li>Voir sa liste d'amis</li>
<li>La déconnection</li>
</ul>

Un bouton parametres à été prévu afin d'accueillir d'éventuels modification de base sur les reglages tels que la langues ou la couleur de theme d'application utiliser mais cette fonctionnalités et désactivé car non développée, le bouton s'en toruve donc désactivé lui aussi.

####Les Fonctionnalitées

######Profil

<ul>
<li>Inscription</li>
Permet l'inscription d'un utilisateur et l'enregistrement de ses données dans la base de données.

<li>Connection</li>
Permet la récupération des données de l'utilisateur dans la base de données afin de les afficher et de les utiliser pour les fonctionnalités suivantes.

<li>Modification</li>
Permet la modification des données retenue dans la base de données.

</ul>

######Friend

<ul>
<li>Affichage</li>
Permet laffichage des utilisateurs considérer comme amis, en attente ou requereur d'un lien d'amitié

<li>Demande</li>
Permet la demande de lien à un autre utilisateur, insertion dans la base de donnée de la ligne (mail de l'utilisateur demandeur , 0, mail de l'utilisateur demandé).

<li>Acceptation/Refus de requete</li>
Permet la réponse à la demande du lien d'amitié d'un utilisateur.
L'acceptation induit un update de la ligne précédemment entré dans la base de donnée pour modifier le 0 ne 1.
Le refus entraine la suppression de la ligne.

<li>Suppression</li>
Permet la suppression d'un lien d'amitié par la suppression de la ligne relative dans la base de donnée

</ul>

######WishList

<ul>
<li>Création</li>
Permet la création d'une wishlist et l'enregistrement de ses données dans la base de données.

<li>Affichage</li>
Permet l'affichage d'une wishlist et la récupération de ses données dans la base de données.

<li>Modification</li>
Permet la modification des infomartions realtives à la wishlist retenue dans la base de données.

<li>Suppression</li>
Suppression de toutes les informations relatives à une wishlist stockées dans la base de données.

</ul>

######Wish

<ul>
<li>Création</li>
Permet la création d'un wish et l'enregistrement de ses données dans la base de données.

<li>Affichage</li>
Permet l'affichage d'un wish et des données qui lui y sont relatives.

</ul>


