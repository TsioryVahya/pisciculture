<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
                    <h1 class="text-lg font-semibold text-gray-900">${title}</h1>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-3xl mx-auto">
                <div class="bg-white shadow sm:rounded-lg border border-gray-200 p-6">
                    <form action="${pageContext.request.contextPath}/etangs/affecter/save" method="POST" class="space-y-6">
                        <input type="hidden" name="etangId" value="${etang.id}">
                        
                        <div>
                            <h3 class="text-lg font-medium text-gray-900">Étang #${etang.id}</h3>
                            <p class="mt-1 text-sm text-gray-500">Surface: ${etang.surface} m² | Capacité: ${etang.capacite}</p>
                        </div>

                        <div class="space-y-4">
                            <label class="block text-sm font-medium text-gray-700">Sélectionnez les poissons à affecter</label>
                            <div class="bg-gray-50 border border-gray-300 rounded-md overflow-hidden">
                                <ul class="divide-y divide-gray-200 max-h-96 overflow-y-auto">
                                    <c:forEach items="${poissons}" var="p">
                                        <li class="px-4 py-3 flex items-center hover:bg-gray-100 transition-colors">
                                            <input type="checkbox" name="poissonIds" value="${p.id}" id="poisson_${p.id}" class="h-4 w-4 text-emerald-600 focus:ring-emerald-500 border-gray-300 rounded">
                                            <label for="poisson_${p.id}" class="ml-3 block text-sm text-gray-900 flex-1 cursor-pointer">
                                                <span class="font-medium">${p.nom}</span>
                                                <span class="ml-2 text-gray-500">(${p.race.nom})</span>
                                            </label>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${empty poissons}">
                                        <li class="px-4 py-8 text-center text-sm text-gray-500 italic">
                                            Aucun poisson disponible pour l'affectation.
                                        </li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>

                        <div class="flex justify-end space-x-3">
                            <a href="${pageContext.request.contextPath}/etangs" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                Annuler
                            </a>
                            <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500" ${empty poissons ? 'disabled' : ''}>
                                Affecter les poissons
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
