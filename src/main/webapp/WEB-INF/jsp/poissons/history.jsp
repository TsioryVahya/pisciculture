<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="fr" class="h-full bg-white">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full overflow-hidden flex">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
        <header class="bg-white border-b border-gray-200">
            <div class="px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16 items-center">
                    <div class="flex items-center">
                        <a href="${pageContext.request.contextPath}/poissons" class="mr-4 text-gray-400 hover:text-gray-500">
                            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                            </svg>
                        </a>
                        <h1 class="text-lg font-semibold text-gray-900">${title}</h1>
                    </div>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-7xl mx-auto space-y-6">
                
                <!-- Résumé / Fiche Poisson -->
                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <div class="px-4 py-5 sm:px-6 border-b border-gray-200 flex justify-between items-center bg-gray-50">
                        <div>
                            <h3 class="text-lg leading-6 font-bold text-gray-900">Fiche Poisson : ${poisson.nom}</h3>
                            <p class="mt-1 max-w-2xl text-sm text-gray-500">Informations générales et état actuel.</p>
                        </div>
                        <div class="flex space-x-2">
                            <span class="px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${poisson.currentStatut.nom == 'Vivant' ? 'bg-green-100 text-green-800' : (poisson.currentStatut.nom == 'Vendu' ? 'bg-blue-100 text-blue-800' : 'bg-red-100 text-red-800')}">
                                ${poisson.currentStatut.nom}
                            </span>
                        </div>
                    </div>
                    <div class="px-4 py-5 sm:p-0">
                        <dl class="sm:divide-y sm:divide-gray-200">
                            <div class="py-4 sm:py-5 sm:grid sm:grid-cols-4 sm:gap-4 sm:px-6">
                                <dt class="text-sm font-medium text-gray-500">Race</dt>
                                <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-1">${poisson.race.nom}</dd>
                                <dt class="text-sm font-medium text-gray-500">Poids Initial</dt>
                                <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-1 font-semibold">
                                    <fmt:formatNumber value="${poisson.poidsInitial}" pattern="#,##0.000000" /> kg
                                </dd>
                            </div>
                            <div class="py-4 sm:py-5 sm:grid sm:grid-cols-4 sm:gap-4 sm:px-6">
                                <dt class="text-sm font-medium text-gray-500">Étang Actuel</dt>
                                <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-1">
                                    <c:choose>
                                        <c:when test="${not empty poisson.currentEtang}">
                                            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                                                Étang #${poisson.currentEtang.id}
                                            </span>
                                        </c:when>
                                        <c:otherwise><span class="text-gray-400 italic">Non assigné</span></c:otherwise>
                                    </c:choose>
                                </dd>
                                <dt class="text-sm font-medium text-gray-500">Poids Actuel</dt>
                                <dd class="mt-1 text-sm text-emerald-600 sm:mt-0 sm:col-span-1 font-bold text-lg">
                                    <fmt:formatNumber value="${poisson.currentPoids}" pattern="#,##0.000000" /> kg
                                </dd>
                            </div>
                        </dl>
                    </div>
                </div>

                <!-- Détails de la Race -->
                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
                        <h3 class="text-sm font-semibold text-gray-900 uppercase tracking-wider">Caractéristiques de la Race : ${poisson.race.nom}</h3>
                    </div>
                    <div class="px-4 py-4 sm:px-6">
                        <div class="grid grid-cols-2 gap-4 sm:grid-cols-4">
                            <div>
                                <p class="text-xs text-gray-500 uppercase">Prix Achat</p>
                                <p class="text-sm font-medium text-gray-900"><fmt:formatNumber value="${poisson.race.prixAchatParKg}" pattern="#,##0.00" /> Ar/kg</p>
                            </div>
                            <div>
                                <p class="text-xs text-gray-500 uppercase">Prix Vente</p>
                                <p class="text-sm font-medium text-gray-900"><fmt:formatNumber value="${poisson.race.prixVenteParKg}" pattern="#,##0.00" /> Ar/kg</p>
                            </div>
                            <div>
                                <p class="text-xs text-gray-500 uppercase">Poids Max</p>
                                <p class="text-sm font-medium text-gray-900">${poisson.race.poidsMax} kg</p>
                            </div>
                            <div>
                                <p class="text-xs text-gray-500 uppercase">Besoins Nutritionnels</p>
                                <div class="flex flex-wrap gap-1 mt-1">
                                    <c:forEach items="${poisson.race.nutriments}" var="rn">
                                        <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-emerald-100 text-emerald-800">
                                            ${rn.nutriment.nom}: ${rn.besoinNutriment} g
                                        </span>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
                    <!-- Historique des Statuts -->
                    <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                        <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
                            <h3 class="text-lg leading-6 font-medium text-gray-900">Historique des Statuts</h3>
                        </div>
                        <ul class="divide-y divide-gray-200">
                            <c:forEach items="${statutHistory}" var="sh">
                                <li class="px-4 py-4 sm:px-6">
                                    <div class="flex items-center justify-between">
                                        <p class="text-sm font-medium text-emerald-600 truncate">${sh.statut.nom}</p>
                                        <div class="ml-2 flex-shrink-0 flex">
                                            <p class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                                                <fmt:parseDate value="${sh.dateChangement.toString().substring(0,16)}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm" />
                                            </p>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                    <!-- Historique des Étangs -->
                    <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                        <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
                            <h3 class="text-lg leading-6 font-medium text-gray-900">Historique des Étangs</h3>
                        </div>
                        <ul class="divide-y divide-gray-200">
                            <c:forEach items="${etangHistory}" var="eh">
                                <li class="px-4 py-4 sm:px-6">
                                    <div class="flex items-center justify-between">
                                        <p class="text-sm font-medium text-indigo-600 truncate">Étang #${eh.etang.id}</p>
                                        <div class="ml-2 flex-shrink-0 flex">
                                            <p class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                                                <c:choose>
                                                    <c:when test="${eh.date.toString().length() >= 10}">
                                                        <fmt:parseDate value="${eh.date.toString().substring(0,10)}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                                        <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                                    </c:when>
                                                    <c:otherwise>${eh.date}</c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <!-- Historique d'Évolution -->
                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <div class="px-4 py-5 sm:px-6 border-b border-gray-200 flex flex-col sm:flex-row justify-between items-center gap-4">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">Évolution du Poids et Stocks Nutritionnels</h3>
                        
                        <!-- Formulaire de Recherche par Date -->
                        <form action="${pageContext.request.contextPath}/poissons/history/${poisson.id}" method="get" class="flex items-center gap-2">
                            <label for="searchDate" class="sr-only">Chercher par date</label>
                            <input type="date" name="searchDate" id="searchDate" value="${searchDate}" 
                                   class="block w-full rounded-md border-gray-300 shadow-sm focus:border-emerald-500 focus:ring-emerald-500 sm:text-sm border p-2">
                            <button type="submit" class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md shadow-sm text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                <svg class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                                </svg>
                                Chercher
                            </button>
                            <c:if test="${not empty searchDate}">
                                <a href="${pageContext.request.contextPath}/poissons/history/${poisson.id}" 
                                   class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm leading-4 font-medium rounded-md shadow-sm text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                    Réinitialiser
                                </a>
                            </c:if>
                        </form>
                    </div>
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Atteint (kg)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stocks Nutritionnels (g)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cycles</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Coût Alim. Cumulé</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach items="${evolutionHistory}" var="ev">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        <fmt:parseDate value="${ev.dateJour.toString()}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                        <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-bold">
                                        <fmt:formatNumber value="${ev.poids}" pattern="#,##0.000000" /> kg
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm">
                                        <div class="flex flex-wrap gap-1">
                                            <c:forEach items="${stocksParJour[ev.id]}" var="s">
                                                <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-emerald-100 text-emerald-800">
                                                    ${s.nutriment.nom}: <fmt:formatNumber value="${s.stock}" pattern="#,##0.00" /> g
                                                </span>
                                            </c:forEach>
                                            <c:if test="${empty stocksParJour[ev.id]}">
                                                <span class="text-xs text-gray-400 italic">Aucun stock</span>
                                            </c:if>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        ${ev.cyclesComplets} j / ${ev.demiCycles} ½j
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-emerald-800 font-medium">
                                        <fmt:formatNumber value="${ev.coutAlimentationCumule}" pattern="#,##0.00" /> Ar
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty evolutionHistory}">
                                <tr>
                                    <td colspan="6" class="px-6 py-10 text-center text-sm text-gray-500 italic">
                                        Aucun historique de poids enregistré pour ce poisson.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

            </div>
        </main>
    </div>
</body>
</html>
