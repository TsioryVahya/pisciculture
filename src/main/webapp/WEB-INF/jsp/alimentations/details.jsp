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
                        <a href="${pageContext.request.contextPath}/alimentations" class="mr-4 text-gray-400 hover:text-gray-500">
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
                
                <!-- Résumé de l'Alimentation -->
                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <div class="px-4 py-5 sm:px-6 border-b border-gray-200 bg-gray-50">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">Résumé de la Saisie</h3>
                    </div>
                    <div class="px-4 py-5 sm:p-0">
                        <dl class="sm:divide-y sm:divide-gray-200">
                            <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                                <dt class="text-sm font-medium text-gray-500">Date & Heure</dt>
                                <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                                    <fmt:parseDate value="${alimentation.dateHeure.toString().substring(0,16)}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm" />
                                </dd>
                            </div>
                            <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                                <dt class="text-sm font-medium text-gray-500">Étang concerné</dt>
                                <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">Étang #${alimentation.etang.id}</dd>
                            </div>
                        </dl>
                    </div>
                </div>

                <!-- Détails des Nourritures utilisées -->
                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">Nourritures Distribuées</h3>
                    </div>
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nourriture</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantité (kg)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Prix Unitaire</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:set var="totalAchat" value="0" />
                            <c:forEach items="${details}" var="d">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${d.nourriture.nom}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-bold">
                                        <fmt:formatNumber value="${d.quantiteKg}" pattern="#,##0.00" /> kg
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        <fmt:formatNumber value="${d.prixKiloNourriture}" pattern="#,##0.00" /> Ar/kg
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-medium">
                                        <fmt:formatNumber value="${d.quantiteKg * d.prixKiloNourriture}" pattern="#,##0.00" /> Ar
                                        <c:set var="totalAchat" value="${totalAchat + (d.quantiteKg * d.prixKiloNourriture)}" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot class="bg-gray-50">
                            <tr>
                                <td colspan="3" class="px-6 py-3 text-right text-sm font-bold text-gray-900 uppercase">Coût Total de l'alimentation :</td>
                                <td class="px-6 py-3 text-left text-sm font-bold text-emerald-600">
                                    <fmt:formatNumber value="${totalAchat}" pattern="#,##0.00" /> Ar
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>

                <!-- État Nutrition Jour (Poissons de l'étang) -->
                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">Consommation par Poisson et Croissance (État Nutrition Jour)</h3>
                    </div>
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poisson</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Race</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stock Protéines (g)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stock Glucides (g)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Actuel (kg)</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:set var="found" value="false" />
                            <c:forEach items="${etatsJour}" var="ej">
                                <%-- On ne filtre pas par étang ici car le contrôleur ne passe pas l'étang, mais ej.poisson.currentEtang pourrait aider si disponible --%>
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">
                                        <a href="${pageContext.request.contextPath}/poissons/history/${ej.poisson.id}" class="hover:underline">${ej.poisson.nom}</a>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${ej.poisson.race.nom}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-emerald-600 font-medium">
                                        <fmt:formatNumber value="${ej.protStock}" pattern="#,##0.00" /> g
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-blue-600 font-medium">
                                        <fmt:formatNumber value="${ej.glucStock}" pattern="#,##0.00" /> g
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-bold">
                                        <fmt:formatNumber value="${ej.poids}" pattern="#,##0.000000" /> kg
                                    </td>
                                </tr>
                                <c:set var="found" value="true" />
                            </c:forEach>
                            <c:if test="${not found}">
                                <tr>
                                    <td colspan="5" class="px-6 py-10 text-center text-sm text-gray-500">
                                        Aucun état nutritionnel trouvé pour cette date.
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
